package aprimorar.pessoas.service;

import aprimorar.pessoas.api.AlunoService;
import aprimorar.pessoas.domain.AlunoEntity;
import aprimorar.pessoas.domain.exception.AlunoDuplicadoException;
import aprimorar.pessoas.domain.exception.AlunoNaoEncontradoException;
import aprimorar.pessoas.repository.AlunoRepository;
import aprimorar.pessoas.repository.specifications.AlunoSpecifications;
import aprimorar.pessoas.web.dto.aluno.AlunoFiltroRequest;
import aprimorar.pessoas.web.dto.aluno.AlunoDetailResponseDTO;
import aprimorar.pessoas.web.dto.aluno.AlunoListResponseDTO;
import aprimorar.pessoas.web.dto.aluno.AlunoRequestDTO;
import aprimorar.pessoas.web.dto.aluno.AlunosListDTO;

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
    public boolean existsById(UUID alunoId) {
        return alunoRepo.existsById(alunoId);
    }

    @Transactional(readOnly = true)
    public Page<AlunoListResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable) {
        Specification<AlunoEntity> spec = AlunoSpecifications.comFiltros(filtro);
        Page<AlunoEntity> alunosPage = alunoRepo.findAll(spec, pageable);

        log.info("Consulta de alunos finalizada, {} registros encontrados.", alunosPage.getTotalElements());
        return alunosPage.map(AlunoListResponseDTO::from);
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
    public AlunoDetailResponseDTO findAlunoById(UUID alunoId) {
        AlunoEntity aluno = findAlunoOrThrow(alunoId);
        log.info("Aluno {} consultado com sucesso.", aluno.getNome());
        return AlunoDetailResponseDTO.from(aluno);
    }

    @Transactional
    public UUID createAluno(AlunoRequestDTO dto) {
        AlunoEntity aluno = dto.toEntity();

        if (alunoRepo.existsByCpf(aluno.getCpf())) {
            throw new AlunoDuplicadoException("Já existe um aluno cadastrado com este CPF.");
        }

        if (alunoRepo.existsByEmail(aluno.getEmail())) {
            throw new AlunoDuplicadoException("Já existe um aluno cadastrado com este e-mail.");
        }

        AlunoEntity savedAluno = alunoRepo.save(aluno);

        log.info("Aluno {} cadastrado com sucesso.", savedAluno.getNome().toUpperCase());
        return savedAluno.getId();
    }

    @Transactional
    public void updateAluno(UUID alunoId, AlunoRequestDTO dto) {
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
