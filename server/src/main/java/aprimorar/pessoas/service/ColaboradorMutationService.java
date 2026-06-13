package aprimorar.pessoas.service;

import aprimorar.pessoas.domain.Colaborador;
import aprimorar.pessoas.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.api.ColaboradorResponseDTO;
import aprimorar.pessoas.api.ArchiveColaboradorVerificationEvent;
import aprimorar.pessoas.api.ColaboradorDeletedEvent;
import aprimorar.pessoas.api.DeleteColaboradorVerificationEvent;
import aprimorar.pessoas.repository.ColaboradorRepository;
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
    private final ApplicationEventPublisher eventPublisher;

    public ColaboradorMutationService(
        ColaboradorRepository colaboradorRepo,
        ApplicationEventPublisher eventPublisher
    ) {
        this.colaboradorRepo = colaboradorRepo;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ColaboradorResponseDTO createColaborador(ColaboradorRequestDTO dto) {
        Colaborador colaborador = dto.toEntity();

        if (colaboradorRepo.existsByCpf(colaborador.getCpf())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este CPF.");
        }

        if (colaboradorRepo.existsByEmail(colaborador.getEmail())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador cadastrado com este e-mail.");
        }

        Colaborador savedColaborador = colaboradorRepo.save(colaborador);
        log.info("Colaborador {} cadastrado com sucesso.", savedColaborador.getNome().toUpperCase());
        return ColaboradorResponseDTO.toDto(savedColaborador);
    }

    @Transactional
    public ColaboradorResponseDTO updateColaborador(UUID colaboradorId, ColaboradorRequestDTO dto) {

        Colaborador colaborador = findByIdOrThrow(colaboradorId);
        Colaborador requestedColaborador = dto.toEntity();

        if (colaboradorRepo.existsByEmailAndIdNot(requestedColaborador.getEmail(), colaboradorId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um colaborador utilizando este e-mail.");
        }

        colaborador.update(
            requestedColaborador.getNome(),
            requestedColaborador.getDataNascimento(),
            requestedColaborador.getPix(),
            requestedColaborador.getTelefone(),
            requestedColaborador.getEmail(),
            requestedColaborador.getFuncao(),
            requestedColaborador.getEndereco()
        );

        log.info("Colaborador {} atualizado com sucesso.", colaborador.getNome().toUpperCase());
        return ColaboradorResponseDTO.toDto(colaborador);
    }

    @Transactional
    public void archiveColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        eventPublisher.publishEvent(new ArchiveColaboradorVerificationEvent(colaboradorId));
        colaborador.archive();
        log.info("Colaborador {} arquivado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void unarchiveColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        colaborador.unarchive();
        log.info("Colaborador {} desarquivado com sucesso.", colaborador.getNome().toUpperCase());
    }

    @Transactional
    public void deleteColaborador(UUID colaboradorId) {
        Colaborador colaborador = findByIdOrThrow(colaboradorId);

        eventPublisher.publishEvent(new DeleteColaboradorVerificationEvent(colaboradorId));
        colaboradorRepo.delete(colaborador);
        eventPublisher.publishEvent(new ColaboradorDeletedEvent(colaboradorId));

        log.info(
            "Colaborador {} deletado com sucesso. Eventos transferidos para 'Colaborador Removido'.",
            colaborador.getNome().toUpperCase()
        );
    }

    private Colaborador findByIdOrThrow(UUID colaboradorId) {
        return colaboradorRepo
            .findById(colaboradorId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Colaborador não encontrado no banco de dados"));
    }

}
