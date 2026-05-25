package aprimorar.pessoas.colaborador.internal.application;

import aprimorar.pessoas.colaborador.api.ColaboradorDeletedEvent;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.internal.domain.Colaborador;
import aprimorar.pessoas.colaborador.internal.infrastructure.persistence.ColaboradorRepository;
import aprimorar.pessoas.colaborador.internal.web.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.shared.address.AddressMapper;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.exception.BusinessException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorMutationService {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorMutationService.class);

    private final ColaboradorRepository colaboradorRepo;
    private final ColaboradorMapper colaboradorMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ColaboradorMutationService(
        ColaboradorRepository colaboradorRepo,
        ColaboradorMapper colaboradorMapper,
        ApplicationEventPublisher eventPublisher
    ) {
        this.colaboradorRepo = colaboradorRepo;
        this.colaboradorMapper = colaboradorMapper;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ColaboradorResponseDTO createColaborador(ColaboradorRequestDTO dto) {
        Colaborador colaborador = colaboradorMapper.toEntity(dto);

        if (colaboradorRepo.existsByCpf(MapperUtils.normalizeCpf(colaborador.getCpf()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(MapperUtils.normalizeEmail(colaborador.getEmail()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este e-mail.");
        }

        Colaborador savedColaborador = colaboradorRepo.save(colaborador);

        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getName().toUpperCase());
        return colaboradorMapper.toDto(savedColaborador);
    }

    @Transactional
    public ColaboradorResponseDTO updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        if (colaboradorRepo.existsByEmailAndIdNot(MapperUtils.normalizeEmail(dto.email()), colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador utilizando este e-mail.");
        }

        validateNotSystemRecord(colaborador, "alterado");

        colaborador.update(
            dto.name(),
            dto.birthdate(),
            dto.pix(),
            dto.contact(),
            dto.email(),
            dto.duty(),
            AddressMapper.toEntity(dto.address())
        );

        log.info("Colaborador {} atualizado com sucesso.", colaborador.getName().toUpperCase());
        return colaboradorMapper.toDto(colaborador);
    }

    @Transactional
    public void archiveColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        colaborador.archive();
        log.info("Colaborador {} arquivado com sucesso.", colaborador.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        colaborador.unarchive();
        log.info("Colaborador {} desarquivado com sucesso.", colaborador.getName().toUpperCase());
    }

    @Transactional
    public void deleteColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        validateNotSystemRecord(colaborador, "deletado");

        colaboradorRepo.delete(colaborador);
        eventPublisher.publishEvent(new ColaboradorDeletedEvent(colaboradorId));

        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para 'Colaborador Removido'.",
            colaborador.getName().toUpperCase()
        );
    }

    private Colaborador findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado no banco de dados"));
    }

    private void validateNotSystemRecord(Colaborador colaborador, String action) {
        if (colaborador.isSystemRecord()) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro 'Colaborador Removido' não pode ser " + action + ".");
        }
    }
}
