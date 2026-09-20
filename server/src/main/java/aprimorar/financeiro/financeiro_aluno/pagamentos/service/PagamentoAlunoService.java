package aprimorar.financeiro.financeiro_aluno.pagamentos.service;

import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.CobrancaAluno;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.PagamentoAluno;
import aprimorar.financeiro.financeiro_aluno.cobrancas.domain.exception.CobrancaAlunoNaoEncontradoException;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.exception.PagamentoAlunoDadosInvalidosException;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.exception.PagamentoAlunoNaoEncontradoException;
import aprimorar.financeiro.financeiro_aluno.cobrancas.repository.CobrancaAlunoRepository;
import aprimorar.financeiro.financeiro_aluno.pagamentos.repository.PagamentoAlunoRepository;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.PagamentoAlunoDetalheResponse;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.PagamentoAlunoResponse;
import aprimorar.financeiro.financeiro_aluno.pagamentos.web.dto.RegistrarPagamentoAlunoRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoAlunoService {

    private final CobrancaAlunoRepository cobrancaRepository;
    private final PagamentoAlunoRepository pagamentoRepository;

    public PagamentoAlunoService(
        CobrancaAlunoRepository cobrancaRepository,
        PagamentoAlunoRepository pagamentoRepository
    ) {
        this.cobrancaRepository = cobrancaRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public void registrar(RegistrarPagamentoAlunoRequest request) {
        if (request == null) {
            throw new PagamentoAlunoDadosInvalidosException("Dados do pagamento são obrigatórios");
        }

        List<Long> ids = request.cobrancaIds();
        if (ids.isEmpty()) {
            throw new PagamentoAlunoDadosInvalidosException("Informe ao menos uma cobrança");
        }
        if (ids.size() != ids.stream().distinct().count()) {
            throw new PagamentoAlunoDadosInvalidosException(
                "Não informe a mesma cobrança mais de uma vez"
            );
        }

        List<CobrancaAluno> cobrancas = cobrancaRepository.findAllByIdInForUpdate(ids);
        if (cobrancas.size() != ids.size()) {
            throw new CobrancaAlunoNaoEncontradoException();
        }
        if (cobrancas.stream().map(CobrancaAluno::getAlunoId).distinct().count() > 1) {
            throw new PagamentoAlunoDadosInvalidosException(
                "Todas as cobranças precisam pertencer ao mesmo aluno"
            );
        }

        UUID alunoId = cobrancas.getFirst().getAlunoId();
        PagamentoAluno pagamento = pagamentoRepository.save(
            new PagamentoAluno(
                alunoId,
                request.dataPagamento(),
                request.formaPagamento(),
                request.comprovanteUrl()
            )
        );
        cobrancas.forEach(c -> c.registrarPagamento(pagamento));
    }

    @Transactional
    public void cancelar(UUID pagamentoId) {
        if (pagamentoId == null) {
            throw new PagamentoAlunoDadosInvalidosException("ID do pagamento é obrigatório");
        }

        PagamentoAluno pagamento = pagamentoRepository.findByIdForUpdate(pagamentoId)
            .orElseThrow(PagamentoAlunoNaoEncontradoException::new);

        List<CobrancaAluno> cobrancas = cobrancaRepository
            .findAllByPagamentoIdForUpdate(pagamento.getId());
        if (cobrancas.isEmpty()) {
            throw new PagamentoAlunoDadosInvalidosException(
                "O pagamento não possui cobranças vinculadas"
            );
        }

        cobrancas.forEach(CobrancaAluno::cancelarPagamento);
        pagamentoRepository.delete(pagamento);
    }

    @Transactional(readOnly = true)
    public Page<PagamentoAlunoResponse> buscarPagamentos(UUID alunoId, Pageable pageable) {
        if (alunoId == null) {
            throw new PagamentoAlunoDadosInvalidosException("ID do aluno é obrigatório");
        }

        return pagamentoRepository.findPagamentosPorAlunoId(alunoId, pageable)
            .map(PagamentoAlunoResponse::from);
    }

    @Transactional(readOnly = true)
    public PagamentoAlunoDetalheResponse buscarPorId(UUID pagamentoId) {
        if (pagamentoId == null) {
            throw new PagamentoAlunoDadosInvalidosException("ID do pagamento é obrigatório");
        }

        PagamentoAluno pagamento = pagamentoRepository.findById(pagamentoId)
            .orElseThrow(PagamentoAlunoNaoEncontradoException::new);
        List<CobrancaAluno> cobrancas = cobrancaRepository
            .findAllByPagamentoIdOrderByIdAsc(pagamentoId);

        return PagamentoAlunoDetalheResponse.from(pagamento, cobrancas);
    }
}
