package aprimorar.pessoas.service;

import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.aluno.AlunoRequestDTO;
import aprimorar.pessoas.dto.aluno.AlunoResponseDTO;
import aprimorar.pessoas.repository.AlunoRepository;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AlunoMutationService.class);
    private static final UUID GHOST_STUDENT_ID = UUID.fromString("00000000-0000-4000-8000-000000000002");

    private final AlunoRepository alunoRepo;

    public AlunoMutationService(AlunoRepository alunoRepo) {
        this.alunoRepo = alunoRepo;
    }

    @Transactional
    public AlunoResponseDTO createAluno(AlunoRequestDTO dto) {
        Aluno aluno = dto.toEntity();

        if (alunoRepo.existsByCpf(aluno.getCpf())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno cadastrado com este CPF.");
        }

        if (alunoRepo.existsByEmail(aluno.getEmail())) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno cadastrado com este e-mail.");
        }

        //TODO FIND RESPONSAVELBY ID

        Aluno savedAluno = alunoRepo.save(aluno);

        log.info("Aluno {} cadastrado com sucesso.", savedAluno.getNome().toUpperCase());
        return AlunoResponseDTO.toDto(savedAluno);
    }

    @Transactional
    public AlunoResponseDTO updateAluno(UUID alunoId, AlunoRequestDTO dto) {
        Aluno aluno = findAlunoOrThrow(alunoId);
        Aluno requestedAluno = dto.toEntity();

        if (GHOST_STUDENT_ID.equals(alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Não é possível modificar o registro de sistema 'Aluno Removido'.");
        }

        if (alunoRepo.existsByCpfAndIdNot(requestedAluno.getCpf(), alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno utilizando este CPF.");
        }

        if (alunoRepo.existsByEmailAndIdNot(requestedAluno.getEmail(), alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Já existe um aluno utilizando este e-mail.");
        }

        aluno.update(
            requestedAluno.getNome(),
            requestedAluno.getDataNascimento(),
            requestedAluno.getTelefone(),
            requestedAluno.getEmail(),
            requestedAluno.getEscola(),
            requestedAluno.getResponsavelId(),
            requestedAluno.getEndereco()
        );

        log.info("Aluno {} atualizado com sucesso.", aluno.getNome().toUpperCase());
        return AlunoResponseDTO.toDto(aluno);
    }

    @Transactional
    public void archiveAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        log.info("Verificando se aluno não tem cobranças pendentes {}.", aluno.getNome().toUpperCase());

        aluno.archive();
        log.info("Aluno {} arquivado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void unarchiveAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);
        ensureNotGhost(alunoId);

        aluno.unarchive();
        log.info("Aluno {} desarquivado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void deleteAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        ensureNotGhost(alunoId);

        log.info("Verificando se aluno não tem cobranças pendentes {}.", aluno.getNome().toUpperCase());

        log.info("Realocando atendimentos do aluno ao Aluno Fantasma.", aluno.getNome().toUpperCase());

        alunoRepo.delete(aluno);
        log.info("Aluno {} deletado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void deleteAlunos(List<Aluno> alunos) {
        alunoRepo.deleteAll(alunos);
        log.info("Alunos deletados com sucesso.");
    }

    /* ----- Helper Methods ----- */
    private Aluno findAlunoOrThrow(UUID alunoId) {
        return alunoRepo
            .findById(alunoId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }

    private void ensureNotGhost(UUID id) {
        if (GHOST_STUDENT_ID.equals(id)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }
    }
}
