package aprimorar.pessoas.aluno;

import aprimorar.pessoas.Aluno;
import aprimorar.pessoas.AlunoService;
import aprimorar.pessoas.aluno.domain.AlunoEntity;
import aprimorar.pessoas.aluno.domain.exception.AlunoDuplicadoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoEstadoInvalidoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoNaoEncontradoException;
import aprimorar.pessoas.aluno.repository.AlunoRepository;
import aprimorar.pessoas.aluno.repository.specifications.AlunoSpecifications;
import aprimorar.pessoas.aluno.web.dto.AlunoFiltroRequest;
import aprimorar.pessoas.aluno.web.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.web.dto.AlunoResponseDTO;
import aprimorar.pessoas.aluno.web.dto.AlunosKpisDTO;
import aprimorar.pessoas.aluno.web.dto.AlunosListDTO;
import aprimorar.pessoas.responsavel.domain.ResponsavelEntity;
import aprimorar.pessoas.responsavel.domain.exception.ResponsavelNaoEncontradoException;
import aprimorar.pessoas.responsavel.repository.ResponsavelRepository;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoServiceImpl implements AlunoService {

    private static final Logger log = LoggerFactory.getLogger(AlunoServiceImpl.class);

    private final AlunoRepository alunoRepo;
    private final ResponsavelRepository responsavelRepo;
    private final UUID ghostStudentId;

    public AlunoServiceImpl(
        AlunoRepository alunoRepo,
        ResponsavelRepository responsavelRepo,
        @Value("${aprimorar.ghost-student-id}") String ghostStudentId
    ) {
        this.alunoRepo = alunoRepo;
        this.responsavelRepo = responsavelRepo;
        this.ghostStudentId = UUID.fromString(ghostStudentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Aluno buscarPorId(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);
        return new Aluno(aluno.getId(), aluno.getNome(), aluno.getEscola(), aluno.getActive());
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable) {
        Specification<AlunoEntity> spec = AlunoSpecifications.comFiltros(filtro, ghostStudentId);
        Page<AlunoEntity> alunosPage = alunoRepo.findAll(spec, pageable);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", alunosPage.getTotalElements());
        return alunosPage.map(AlunoResponseDTO::toDto);
    }

    @Transactional(readOnly = true)
    public AlunosKpisDTO getAlunosKpis() {
        long totalAlunos = alunoRepo.countByIdNot(ghostStudentId);
        long totalAlunosAtivos = alunoRepo.countByActiveTrueAndIdNot(ghostStudentId);
        return new AlunosKpisDTO(totalAlunos, totalAlunosAtivos);
    }

    @Transactional(readOnly = true)
    public List<AlunosListDTO> listAlunos() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        List<AlunosListDTO> alunos = alunoRepo
            .findAll(AlunoSpecifications.isNotArchived().and(AlunoSpecifications.isNotGhost(ghostStudentId)), sort)
            .stream()
            .map(aluno -> new AlunosListDTO(aluno.getId(), aluno.getNome()))
            .toList();

        log.info("Consulta de opcoes de alunos finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional(readOnly = true)
    public AlunoResponseDTO findAlunoById(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);
        log.info("Aluno {} consultado com sucesso.", aluno.getNome());
        return AlunoResponseDTO.toDto(aluno);
    }

    @Transactional(readOnly = true)
    public List<AlunoResponseDTO> getAlunosByResponsavelId(UUID responsavelId) {
        List<AlunoResponseDTO> alunos = alunoRepo.findAllByResponsavelId(responsavelId)
            .stream()
            .map(AlunoResponseDTO::toDto)
            .toList();

        log.info("Consulta de alunos por responsavel finalizada, {} registros encontrados.", alunos.size());
        return alunos;
    }

    @Transactional
    public AlunoResponseDTO createAluno(AlunoRequestDTO dto) {
        ResponsavelEntity responsavel = findResponsavelOrThrow(dto.responsavelId());
        AlunoEntity aluno = dto.toEntity(responsavel);

        if (alunoRepo.existsByCpf(aluno.getCpf())) {
            throw new AlunoDuplicadoException("Já existe um aluno cadastrado com este CPF.");
        }

        if (alunoRepo.existsByEmail(aluno.getEmail())) {
            throw new AlunoDuplicadoException("Já existe um aluno cadastrado com este e-mail.");
        }

        AlunoEntity savedAluno = alunoRepo.save(aluno);
        log.info("Aluno {} cadastrado com sucesso.", savedAluno.getNome().toUpperCase());
        return AlunoResponseDTO.toDto(savedAluno);
    }

    @Transactional
    public AlunoResponseDTO updateAluno(UUID alunoId, AlunoRequestDTO dto) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);
        ResponsavelEntity responsavel = findResponsavelOrThrow(dto.responsavelId());
        AlunoEntity requestedAluno = dto.toEntity(responsavel);

        if (ghostStudentId.equals(alunoId)) {
            throw new AlunoEstadoInvalidoException("Não é possível modificar o registro de sistema 'Aluno Removido'.");
        }

        if (alunoRepo.existsByCpfAndIdNot(requestedAluno.getCpf(), alunoId)) {
            throw new AlunoDuplicadoException("Já existe um aluno utilizando este CPF.");
        }

        if (alunoRepo.existsByEmailAndIdNot(requestedAluno.getEmail(), alunoId)) {
            throw new AlunoDuplicadoException("Já existe um aluno utilizando este e-mail.");
        }

        aluno.update(
            requestedAluno.getNome(),
            requestedAluno.getDataNascimento(),
            requestedAluno.getTelefone(),
            requestedAluno.getEmail(),
            requestedAluno.getEscola(),
            requestedAluno.getResponsavel(),
            requestedAluno.getEndereco()
        );

        log.info("Aluno {} atualizado com sucesso.", aluno.getNome().toUpperCase());
        return AlunoResponseDTO.toDto(aluno);
    }

    @Transactional
    public void archiveAluno(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);

        if (ghostStudentId.equals(alunoId)) {
            throw new AlunoEstadoInvalidoException("O registro não pode ser modificado.");
        }

        aluno.archive();
        log.info("Aluno {} arquivado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void unarchiveAluno(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);

        if (ghostStudentId.equals(alunoId)) {
            throw new AlunoEstadoInvalidoException("O registro não pode ser modificado.");
        }

        aluno.unarchive();
        log.info("Aluno {} desarquivado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void deleteAluno(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);

        if (ghostStudentId.equals(alunoId)) {
            throw new AlunoEstadoInvalidoException("O registro não pode ser modificado.");
        }

        alunoRepo.delete(aluno);
        log.info("Aluno {} deletado com sucesso.", aluno.getNome().toUpperCase());
    }

    private AlunoEntity findAlunoOrThrow(UUID alunoId) {
        return alunoRepo.findById(alunoId)
            .orElseThrow(() -> new AlunoNaoEncontradoException("Aluno não encontrado no banco de dados"));
    }

    private ResponsavelEntity findResponsavelOrThrow(UUID responsavelId) {
        return responsavelRepo.findById(responsavelId)
            .orElseThrow(() -> new ResponsavelNaoEncontradoException("Responsável não encontrado no banco de dados"));
    }
}
