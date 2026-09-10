package aprimorar.pessoas.aluno.service;

import aprimorar.pessoas.aluno.api.Aluno;
import aprimorar.pessoas.aluno.api.AlunoService;
import aprimorar.pessoas.aluno.domain.AlunoEntity;
import aprimorar.pessoas.aluno.domain.exception.AlunoDuplicadoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoNaoEncontradoException;
import aprimorar.pessoas.aluno.repository.AlunoRepository;
import aprimorar.pessoas.aluno.repository.specifications.AlunoSpecifications;
import aprimorar.pessoas.aluno.web.dto.AlunoFiltroRequest;
import aprimorar.pessoas.aluno.web.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.web.dto.AlunoResponseDTO;
import aprimorar.pessoas.aluno.web.dto.AlunosListDTO;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public AlunoServiceImpl(AlunoRepository alunoRepo) {
        this.alunoRepo = alunoRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public Aluno buscarPorId(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);

        return new Aluno(
            aluno.getId(),
            aluno.getNome(),
            aluno.getEscola(),
            aluno.getActive());
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable) {
        Specification<AlunoEntity> spec = AlunoSpecifications.comFiltros(filtro);
        Page<AlunoEntity> alunosPage = alunoRepo.findAll(spec, pageable);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", alunosPage.getTotalElements());
        return alunosPage.map(AlunoResponseDTO::toDto);
    }

    @Transactional(readOnly = true)
    public List<AlunosListDTO> listAlunos() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        List<AlunosListDTO> alunos = alunoRepo
            .findAll(AlunoSpecifications.isActive(), sort)
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

    @Transactional
    public AlunoResponseDTO createAluno(AlunoRequestDTO dto) {
        AlunoEntity aluno = dto.toEntity();

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
        AlunoEntity requestedAluno = dto.toEntity();

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
    public void deactivateAluno(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);
        //TODO: Verificar se aluno tem pagamentos pendentes
        aluno.deactivate();
        log.info("Aluno {} desativado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void activateAluno(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);
        aluno.activate();
        log.info("Aluno {} ativado com sucesso.", aluno.getNome().toUpperCase());
    }

    private AlunoEntity findAlunoOrThrow(UUID alunoId) {
        return alunoRepo.findById(alunoId)
            .orElseThrow(() -> new AlunoNaoEncontradoException("Aluno não encontrado no banco de dados"));
    }

}
