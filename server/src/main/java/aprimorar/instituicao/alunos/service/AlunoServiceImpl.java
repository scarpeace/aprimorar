package aprimorar.instituicao.alunos.service;

import aprimorar.financeiro.api.cobrancas.CobrancaApi;
import aprimorar.instituicao.alunos.domain.Aluno;
import aprimorar.instituicao.alunos.domain.exception.AlunoDuplicadoException;
import aprimorar.instituicao.alunos.domain.exception.AlunoNaoEncontradoException;
import aprimorar.instituicao.alunos.domain.exception.AlunoPossuiPendenciaFinanceiraException;
import aprimorar.instituicao.alunos.repository.AlunoRepository;
import aprimorar.instituicao.alunos.repository.AlunoSpecifications;
import aprimorar.instituicao.alunos.web.dto.aluno.AlunoDetailResponseDTO;
import aprimorar.instituicao.alunos.web.dto.aluno.AlunoFiltroRequest;
import aprimorar.instituicao.alunos.web.dto.aluno.AlunoListResponseDTO;
import aprimorar.instituicao.alunos.web.dto.aluno.AlunoRequestDTO;
import aprimorar.instituicao.alunos.web.dto.aluno.AlunosListDTO;

import java.util.List;
import java.util.Optional;
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
public class AlunoServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AlunoServiceImpl.class);

    private final AlunoRepository alunoRepo;
    private final CobrancaApi cobrancaApi;

    public AlunoServiceImpl(AlunoRepository alunoRepo, CobrancaApi cobrancaApi) {
        this.alunoRepo = alunoRepo;
        this.cobrancaApi = cobrancaApi;
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID alunoId) {
        return alunoRepo.existsById(alunoId);
    }

    @Transactional(readOnly = true)
    public Optional<Aluno> findEntityById(UUID alunoId) {
        return alunoRepo.findById(alunoId);
    }

    @Transactional(readOnly = true)
    public Page<AlunoListResponseDTO> getAlunos(AlunoFiltroRequest filtro, Pageable pageable) {
        Specification<Aluno> spec = AlunoSpecifications.comFiltros(filtro);
        Page<Aluno> alunosPage = alunoRepo.findAll(spec, pageable);

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
        Aluno aluno = findAlunoOrThrow(alunoId);
        log.info("Aluno {} consultado com sucesso.", aluno.getNome());
        return AlunoDetailResponseDTO.from(aluno);
    }

    @Transactional
    public UUID createAluno(AlunoRequestDTO dto) {
        Aluno aluno = dto.toEntity();

        if (alunoRepo.existsByCpf(aluno.getCpf())) {
            throw new AlunoDuplicadoException("Já existe um aluno cadastrado com este CPF.");
        }

        if (alunoRepo.existsByEmail(aluno.getEmail())) {
            throw new AlunoDuplicadoException("Já existe um aluno cadastrado com este e-mail.");
        }

        Aluno savedAluno = alunoRepo.save(aluno);

        log.info("Aluno {} cadastrado com sucesso.", savedAluno.getNome().toUpperCase());
        return savedAluno.getId();
    }

    @Transactional
    public void updateAluno(UUID alunoId, AlunoRequestDTO dto) {
        Aluno aluno = findAlunoOrThrow(alunoId);
        Aluno requestedAluno = dto.toEntity();

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
        Aluno aluno = findAlunoOrThrow(alunoId);

        if (cobrancaApi.possuiPendenciaPorAlunoId(alunoId)) {
            throw new AlunoPossuiPendenciaFinanceiraException();
        }

        aluno.deactivate();
        log.info("Aluno {} desativado com sucesso.", aluno.getNome().toUpperCase());
    }

    @Transactional
    public void activateAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);
        aluno.activate();
        log.info("Aluno {} ativado com sucesso.", aluno.getNome().toUpperCase());
    }

    private Aluno findAlunoOrThrow(UUID alunoId) {
        return alunoRepo.findById(alunoId)
            .orElseThrow(() -> new AlunoNaoEncontradoException("Aluno não encontrado no banco de dados"));
    }

}
