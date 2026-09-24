package aprimorar.agendamento.alunos.application;

import aprimorar.financeiro.recebimentos_alunos.api.RecebimentosApi;
import aprimorar.agendamento.alunos.domain.Aluno;
import aprimorar.agendamento.alunos.domain.exception.AlunoNaoEncontradoException;
import aprimorar.agendamento.alunos.domain.exception.AlunoPossuiPendenciaFinanceiraException;
import aprimorar.agendamento.alunos.infrastructure.AlunoRepository;
import aprimorar.agendamento.alunos.infrastructure.AlunoSpecifications;
import aprimorar.agendamento.alunos.web.dto.AlunoFiltroRequest;
import aprimorar.agendamento.alunos.web.dto.AlunoResponse;
import aprimorar.agendamento.alunos.web.dto.AlunoOptionResponse;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlunoService {

    private final AlunoRepository alunoRepo;
    private final RecebimentosApi cobrancaApi;

    public AlunoService(AlunoRepository alunoRepo, RecebimentosApi cobrancaApi) {
        this.alunoRepo = alunoRepo;
        this.cobrancaApi = cobrancaApi;
    }

    @Transactional(readOnly = true)
    public Page<AlunoResponse> getAlunos(AlunoFiltroRequest filtro, Pageable pageable) {
        Specification<Aluno> spec = AlunoSpecifications.comFiltros(filtro);
        return alunoRepo.findAll(spec, pageable).map(AlunoResponse::toDto);
    }

    @Transactional(readOnly = true)
    public List<AlunoOptionResponse> listAlunosOptions() {
        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        return alunoRepo.findAll(AlunoSpecifications.isActive(), sort).stream()
            .map(AlunoOptionResponse::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    public AlunoResponse findAlunoById(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);
        return AlunoResponse.toDto(aluno);
    }

    @Transactional
    public UUID createAluno(Aluno aluno) {
        Aluno savedAluno = alunoRepo.save(aluno);
        return savedAluno.getId();
    }

    @Transactional
    public void updateAluno(UUID alunoId, Aluno requestedAluno) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        aluno.update(
            requestedAluno.getNome(),
            requestedAluno.getDataNascimento(),
            requestedAluno.getTelefone(),
            requestedAluno.getCpf(),
            requestedAluno.getEmail(),
            requestedAluno.getEscola(),
            requestedAluno.getResponsavel(),
            requestedAluno.getEndereco()
        );
    }

    @Transactional
    public void deactivateAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);

        if (cobrancaApi.possuiCobrancaPendente(alunoId)) {
            throw new AlunoPossuiPendenciaFinanceiraException();
        }

        aluno.deactivate();
    }

    @Transactional
    public void activateAluno(UUID alunoId) {
        Aluno aluno = findAlunoOrThrow(alunoId);
        aluno.activate();
    }

    public Aluno findAlunoOrThrow(UUID alunoId) {
        return alunoRepo.findById(alunoId)
            .orElseThrow(() -> new AlunoNaoEncontradoException("Aluno não encontrado no banco de dados"));
    }

}
