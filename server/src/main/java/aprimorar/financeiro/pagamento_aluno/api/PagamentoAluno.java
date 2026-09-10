package aprimorar.financeiro.pagamento_aluno.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoAluno(
    Long id,
    LocalDateTime dataPagamento,
    BigDecimal total,
    BigDecimal desconto,
    FormaPagamento formaPagamento
) {}
