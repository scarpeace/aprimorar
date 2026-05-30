package aprimorar.pessoas.responsavel.internal.application;

import aprimorar.pessoas.aluno.AlunoQueryApi;
import aprimorar.pessoas.responsavel.internal.web.dto.ResponsavelRequestDTO;
import aprimorar.pessoas.responsavel.internal.web.dto.ResponsavelResponseDTO;
import aprimorar.pessoas.responsavel.internal.domain.Responsavel;
import aprimorar.pessoas.responsavel.internal.infrastructure.persistence.ResponsavelRepository;
import aprimorar.shared.MapperUtils;
import aprimorar.shared.exception.BusinessException;

import java.util.UUID;
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
    private final AlunoQueryApi alunoQueryApi;

    public ResponsavelMutationService(
        ResponsavelRepository responsavelRepo,
        ResponsavelMapper responsavelMapper,
        AlunoQueryApi alunoQueryApi
    ) {
        this.responsavelRepo = responsavelRepo;
        this.responsavelMapper = responsavelMapper;
        this.alunoQueryApi = alunoQueryApi;
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

        log.info("Responsável {} cadastrado com sucesso.", savedResponsavel.getName().toUpperCase());
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

        responsavel.updateDetails(dto.name(), dto.birthdate(), dto.pix(), dto.contact(), dto.email());

        log.info("Responsável {} atualizado com sucesso.", responsavel.getName().toUpperCase());
        return responsavelMapper.toResponseDto(responsavel);
    }

    @Transactional
    public void archiveResponsavel(UUID responsavelId) {
        Responsavel responsavel = findResponsavelOrThrow(responsavelId);

        if (alunoQueryApi.hasActiveAlunosLinkedToResponsavel(responsavel.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,"O responsável possui alunos ativos vinculados. Arquive os alunos antes de arquivar o responsável.");
        }

        responsavel.archive();
        log.info("Responsável {} arquivado com sucesso.", responsavel.getName().toUpperCase());
    }

    @Transactional
    public void unarchiveResponsavel(UUID responsavelId) {
        Responsavel responsavel = findResponsavelOrThrow(responsavelId);

        responsavel.unarchive();
        log.info("Responsável {} desarquivado com sucesso.", responsavel.getName().toUpperCase());
    }

    @Transactional
    public void deleteResponsavel(UUID responsavelId) {
        Responsavel responsavel = findResponsavelOrThrow(responsavelId);

        if (alunoQueryApi.hasActiveAlunosLinkedToResponsavel(responsavel.getId())) {
            throw new BusinessException(HttpStatus.BAD_REQUEST,
                "O responsável possui ativos alunos vinculados. Arquive-os ou remova os alunos antes de excluí-lo."
            );
        }

        responsavelRepo.delete(responsavel);
        log.info("Responsável {} deletado com sucesso.", responsavel.getName().toUpperCase());
    }

    private Responsavel findResponsavelOrThrow(UUID responsavelId) {
        return responsavelRepo
            .findById(responsavelId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Responsável não encontrado no banco de dados"));
    }
}
