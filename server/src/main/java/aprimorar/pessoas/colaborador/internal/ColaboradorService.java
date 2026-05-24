package aprimorar.pessoas.colaborador.internal;

import aprimorar.pessoas.colaborador.api.dto.ColaboradorOptionsDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresResponseDTO;
import aprimorar.pessoas.colaborador.api.ColaboradorDeletedEvent;
import aprimorar.pessoas.colaborador.api.ColaboradorQueryApi;
import aprimorar.pessoas.colaborador.internal.repository.ColaboradorRepository;
import aprimorar.pessoas.colaborador.internal.repository.ColaboradorSpecifications;
import aprimorar.pessoas.shared.address.AddressMapper;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.exception.BusinessException;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ColaboradorService implements ColaboradorQueryApi {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorService.class);
    private final ColaboradorRepository colaboradorRepo;
    private final ApplicationEventPublisher eventPublisher;

    public ColaboradorService(
        ColaboradorRepository colaboradorRepo,
        ApplicationEventPublisher eventPublisher
    ) {
        this.colaboradorRepo = colaboradorRepo;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ColaboradorResponseDTO createColaborador(ColaboradorRequestDTO dto) {
        if (colaboradorRepo.existsByCpf(MapperUtils.normalizeCpf(dto.cpf()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(MapperUtils.normalizeEmail(dto.email()))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este e-mail.");
        }

        var colaborador = ColaboradorMapper.toEntity(dto);
        Colaborador savedColaborador = colaboradorRepo.save(colaborador);

        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getName().toUpperCase());
        return ColaboradorMapper.toDto(savedColaborador);
    }

    @Transactional(readOnly = true)
    public ColaboradoresResponseDTO getColaboradores(Pageable pageable, String busca, Boolean arquivado) {
        long colaboradoresAtivos = colaboradorRepo.countByActiveTrue();
        Specification<Colaborador> spec = ColaboradorSpecifications.isNotGhost();

        if (Boolean.TRUE.equals(arquivado)) {
            spec = spec.and(ColaboradorSpecifications.isArchived());
        }

        if (busca != null && !busca.trim().isEmpty()) {
            spec = spec.and(ColaboradorSpecifications.searchContainsIgnoreCase(busca.trim()));
        }

        Page<Colaborador> colaboradoresPage = colaboradorRepo.findAll(spec, pageable);
        ColaboradoresResponseDTO colaboradoresDtoPage = new ColaboradoresResponseDTO(
            colaboradoresAtivos,
            colaboradoresPage.map(ColaboradorMapper::toDto)
        );

        log.info("Consulta de colaboradores finalizada, {} registros encontrados.", colaboradoresPage.getTotalElements());
        return colaboradoresDtoPage;
    }

    @Transactional(readOnly = true)
    public ColaboradorResponseDTO findColaboradorById(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        log.info("Colaborador {} consultado com sucesso.", colaborador.getName().toUpperCase());
        return ColaboradorMapper.toDto(colaborador);
    }

    @Transactional(readOnly = true)
    public List<ColaboradorOptionsDTO> listColaboradores() {
        return colaboradorRepo.findAllByDutyNotAndActiveTrueOrderByNameAsc(ColaboradorDutyEnum.SYSTEM)
            .stream()
            .map(e -> new ColaboradorOptionsDTO(e.getId(), e.getName()))
            .toList();
    }

    @Transactional
    public ColaboradorResponseDTO updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto) {
        var colaborador = findByIdOrThrow(colaboradorId);

        if (colaboradorRepo.existsByEmailAndIdNot(MapperUtils.normalizeEmail(dto.email()), colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador utilizando este e-mail.");
        }

        if (ColaboradorDutyEnum.SYSTEM.equals(colaborador.getDuty())) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro 'Colaborador Removido' não pode ser deletado.");
        }

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
        return ColaboradorMapper.toDto(colaborador);
    }

    @Transactional
    public void deleteColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        if (ColaboradorDutyEnum.SYSTEM.equals(colaborador.getDuty())) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro 'Colaborador Removido' não pode ser deletado.");
        }


        colaboradorRepo.delete(colaborador);
        eventPublisher.publishEvent(new ColaboradorDeletedEvent(colaboradorId));

        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para 'Colaborador Removido'.",
            colaborador.getName().toUpperCase()
        );
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

    /* ----- Helper Methods ----- */
    private Colaborador findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND,"Colaborador não encontrado no banco de dados"));
    }
}
