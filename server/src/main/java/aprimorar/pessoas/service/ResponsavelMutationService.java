package aprimorar.pessoas.service;

import aprimorar.pessoas.domain.Responsavel;
import aprimorar.pessoas.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.events.DeleteAlunoVerificationEvent;
import aprimorar.pessoas.mappers.ResponsavelMapper;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.ResponsavelRepository;
import aprimorar.shared.MapperUtils;
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
    private final ResponsavelMapper responsavelMapper;
    private final AlunoRepository alunoRepo;
    private final AlunoMutationService alunoMutationService;
    private final ApplicationEventPublisher eventPublisher;

    public ResponsavelMutationService(
        ResponsavelRepository responsavelRepo,
        ResponsavelMapper responsavelMapper,
        AlunoRepository alunoRepo,
        AlunoMutationService alunoMutationService,
        ApplicationEventPublisher eventPublisher
    ) {
        this.responsavelRepo = responsavelRepo;
        this.responsavelMapper = responsavelMapper;
        this.alunoRepo = alunoRepo;
        this.alunoMutationService = alunoMutationService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public ResponsavelResponseDTO createResponsavel(ResponsavelRequestDTO dto) {
        Responsavel responsavel = responsavelMapper.toEntity(dto);

        if (Boolean.TRUE.equals(responsavelRepo.existsByCpf(MapperUtils.normalizeCpf(responsavel.getCpf())))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável cadastrado com este CPF.");
        }

        if (Boolean.TRUE.equals(responsavelRepo.existsByEmail(MapperUtils.normalizeEmail(responsavel.getEmail())))) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável cadastrado com este e-mail.");
        }

        Responsavel savedResponsavel = responsavelRepo.save(responsavel);

        log.info("Responsável {} cadastrado com sucesso.", savedResponsavel.getNome().toUpperCase());
        return responsavelMapper.toResponseDto(savedResponsavel);
    }

    @Transactional
    public ResponsavelResponseDTO updateResponsavel(UUID responsavelId, ResponsavelRequestDTO dto) {
        Responsavel responsavel = findResponsavelOrThrow(responsavelId);

        if (responsavelRepo.existsByCpfAndIdNot(MapperUtils.normalizeCpf(dto.cpf()), responsavelId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável utilizando este CPF.");
        }

        if (responsavelRepo.existsByEmailAndIdNot(MapperUtils.normalizeEmail(dto.email()), responsavelId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um responsável utilizando este e-mail.");
        }

        responsavel.updateDetails(dto.nome(), dto.dataNascimento(), dto.telefone(), dto.email());

        log.info("Responsável {} atualizado com sucesso.", responsavel.getNome().toUpperCase());
        return responsavelMapper.toResponseDto(responsavel);
    }

    @Transactional
    public void deleteResponsavel(UUID parentId, boolean cascade) {
        var responsavel = findResponsavelOrThrow(parentId);
        var alunos = alunoRepo.findAllByParentId(parentId);

        if (!alunos.isEmpty() && !cascade) {
            throw new BusinessException(
                HttpStatus.BAD_REQUEST, "Este responsável possui alunos vinculados. Confirme a exclusão em cascata para continuar."
            );
        }

        for (var aluno : alunos) {
            if(Boolean.TRUE.equals(aluno.getActive())){
                throw new BusinessException(HttpStatus.BAD_REQUEST, "Não é possível excluir o responsável com alunos ativos, vincule o aluno a outro responsável ou arquive o aluno antes de seguir.");
            }
            eventPublisher.publishEvent(new DeleteAlunoVerificationEvent(aluno.getId()));
        }

        alunoMutationService.deleteAlunos(alunos);
        responsavelRepo.delete(responsavel);
    }

    private Responsavel findResponsavelOrThrow(UUID responsavelId) {
        return responsavelRepo
            .findById(responsavelId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Responsável não encontrado no banco de dados"));
    }
}
