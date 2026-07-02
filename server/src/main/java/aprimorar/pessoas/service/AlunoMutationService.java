package aprimorar.pessoas.service;

import aprimorar.atendimentos.repository.AtendimentoRepository;
import aprimorar.exception.BusinessException;
import aprimorar.pessoas.domain.Aluno;
import aprimorar.pessoas.dto.aluno.AlunoRequestDTO;
import aprimorar.pessoas.dto.aluno.AlunoResponseDTO;
import aprimorar.pessoas.repository.AlunoRepository;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoMutationService {

    private static final Logger log = LoggerFactory.getLogger(AlunoMutationService.class);

    private final AlunoRepository alunoRepo;
    private final AtendimentoRepository atendimentoRepo;
    private final UUID ghostStudentId;

    public AlunoMutationService(
        AlunoRepository alunoRepo,
        AtendimentoRepository atendimentoRepo,
        @Value("${aprimorar.ghost-student-id}") String ghostStudentId
    ) {
        this.alunoRepo = alunoRepo;
        this.atendimentoRepo = atendimentoRepo;
        this.ghostStudentId = UUID.fromString(ghostStudentId);
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

        if (ghostStudentId.equals(alunoId)) {
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

        if (ghostStudentId.equals(alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }

        log.info("Verificando se aluno {} pode ser arquivado.", aluno.getNome().toUpperCase());
        ensureAlunoPodeSerArquivado(alunoId);

        aluno.archive();
        log.info("Aluno {} arquivado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void unarchiveAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        if (ghostStudentId.equals(alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }

        aluno.unarchive();
        log.info("Aluno {} desarquivado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void deleteAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        if (ghostStudentId.equals(alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "O registro não pode ser modificado.");
        }

        ensureAlunoArquivado(aluno);
        ensureAlunoPodeSerArquivado(alunoId);

        log.info("Realocando atendimentos do aluno ao Aluno Fantasma.", aluno.getNome().toUpperCase());
        atendimentoRepo.reassignAtendimentosAlunoToGhost(alunoId, alunoRepo.getReferenceById(ghostStudentId));

        alunoRepo.delete(aluno);
        log.info("Aluno {} deletado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void deleteAlunos(List<Aluno> alunos) {
        alunos.forEach(aluno -> deleteAluno(aluno.getId()));
        log.info("Alunos deletados com sucesso.");
    }

    /* ----- Helper Methods ----- */
    private Aluno findAlunoOrThrow(UUID alunoId) {
        return alunoRepo
            .findById(alunoId)
            .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Aluno não encontrado no banco de dados"));
    }

    private void ensureAlunoPodeSerArquivado(UUID alunoId) {
        if (atendimentoRepo.alunoPossuiAtendimentoAgendado(alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Não é possível arquivar um aluno com atendimentos agendados.");
        }

        if (atendimentoRepo.alunoPossuiPagamentoAlunoPendente(alunoId)) {
            throw new BusinessException(HttpStatus.CONFLICT, "Não é possível arquivar um aluno com pagamento pendente.");
        }
    }

    private void ensureAlunoArquivado(Aluno aluno) {
        if (!Boolean.FALSE.equals(aluno.getActive())) {
            throw new BusinessException(HttpStatus.CONFLICT, "O aluno precisa estar arquivado antes de ser excluído.");
        }
    }
}
