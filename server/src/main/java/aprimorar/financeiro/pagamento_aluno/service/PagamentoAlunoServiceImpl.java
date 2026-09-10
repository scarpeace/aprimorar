package aprimorar.financeiro.pagamento_aluno.service;

import aprimorar.financeiro.pagamento_aluno.api.FormaPagamento;
import aprimorar.financeiro.pagamento_aluno.api.PagamentoAluno;
import aprimorar.financeiro.pagamento_aluno.api.PagamentoAlunoService;
import aprimorar.financeiro.pagamento_aluno.domain.PagamentoAlunoEntity;
import aprimorar.financeiro.pagamento_aluno.domain.exception.PagamentoAlunoNaoEncontradoException;
import aprimorar.financeiro.pagamento_aluno.repository.PagamentoAlunoRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoAlunoServiceImpl implements PagamentoAlunoService {

    private final PagamentoAlunoRepository pagamentoRepository;

    public PagamentoAlunoServiceImpl(PagamentoAlunoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @Override
    @Transactional
    public PagamentoAluno registrarPagamento(
        BigDecimal total,
        BigDecimal desconto,
        FormaPagamento formaPagamento
    ) {
        PagamentoAlunoEntity pagamento = new PagamentoAlunoEntity(
            LocalDateTime.now(),
            total,
            desconto,
            formaPagamento
        );

        return toContract(pagamentoRepository.save(pagamento));
    }

    @Override
    @Transactional
    public void excluirPagamento(Long pagamentoId) {
        PagamentoAlunoEntity pagamento = pagamentoRepository.findById(pagamentoId)
            .orElseThrow(PagamentoAlunoNaoEncontradoException::new);

        pagamentoRepository.delete(pagamento);
    }

    private PagamentoAluno toContract(PagamentoAlunoEntity entity) {
        return new PagamentoAluno(
            entity.getId(),
            entity.getDataPagamento(),
            entity.getValor(),
            entity.getDesconto(),
            entity.getFormaPagamento()
        );
    }
}
