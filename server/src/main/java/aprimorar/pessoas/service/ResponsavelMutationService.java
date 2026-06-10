package aprimorar.pessoas.service;

import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.events.DeleteAlunoVerificationEvent;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ResponsavelRepository;
import aprimorar.shared.exception.BusinessException;

import java.util.UUID;
import org.springframework.context.ApplicationEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResponsavelMutationService {

    private static final Logger log = LoggerFactory.getLogger(ResponsavelMutationService.class);

    private final ResponsavelRepository responsavelRepo;
    private final AlunoRepository alunoRepo;

    public ResponsavelMutationService(
        ResponsavelRepository responsavelRepo,
        AlunoRepository alunoRepo
    ) {
        this.responsavelRepo = responsavelRepo;
        this.alunoRepo = alunoRepo;
    }

    @Transactional
    public ResponsavelResponseDTO createResponsavel(ResponsavelRequestDTO dto) {
        Responsavel responsavel = dto.toEntity();

        if (responsavelRepo.existsByCpf(responsavel.getCpf())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável cadastrado com este CPF.");
        }

        if (responsavelRepo.existsByEmail(responsavel.getEmail())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável cadastrado com este e-mail.");
        }

        Responsavel savedResponsavel = responsavelRepo.save(responsavel);

        log.info("Responsável {} cadastrado com sucesso.", savedResponsavel.getNome().toUpperCase());
        return ResponsavelResponseDTO.toDto(savedResponsavel);
    }

    @Transactional
    public ResponsavelResponseDTO updateResponsavel(UUID responsavelId, ResponsavelRequestDTO dto) {
        Responsavel responsavel = findResponsavelOrThrow(responsavelId);
        Responsavel requestedResponsavel = dto.toEntity();

        if (responsavelRepo.existsByCpfAndIdNot(requestedResponsavel.getCpf(), responsavelId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável utilizando este CPF.");
        }

        if (responsavelRepo.existsByEmailAndIdNot(requestedResponsavel.getEmail(), responsavelId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável utilizando este e-mail.");
        }

        responsavel.update(
            requestedResponsavel.getNome(),
            requestedResponsavel.getDataNascimento(),
            requestedResponsavel.getTelefone(),
            requestedResponsavel.getEmail()
        );

        log.info("Responsável {} atualizado com sucesso.", responsavel.getNome().toUpperCase());
        return ResponsavelResponseDTO.toDto(responsavel);
    }

    @Transactional
    public void deleteResponsavel(UUID responsavelId, boolean cascade) {
        var responsavel = findResponsavelOrThrow(responsavelId);
        var alunos = alunoRepo.findAllByParentId(responsavelId);

        if (!alunos.isEmpty()) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST, "Este responsável possui alunos vinculados. Exclua os alunos antes de excluir o responsável."
            );
        }

        responsavelRepo.delete(responsavel);
    }

    private Responsavel findResponsavelOrThrow(UUID responsavelId) {
        return responsavelRepo
            .findById(responsavelId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Responsável não encontrado no banco de dados"));
    }
}
