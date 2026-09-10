package aprimorar.financeiro.pagamento_aluno.api;

import java.math.BigDecimal;

public interface PagamentoAlunoService {

    PagamentoAluno registrarPagamento(
        BigDecimal total,
        BigDecimal desconto,
        FormaPagamento formaPagamento
    );

    void excluirPagamento(Long pagamentoId);
}
