package aprimorar.financeiro.api.financeiro_aluno;

import java.math.BigDecimal;

public record CobrancaAlunoResumo(
    Long id,
    BigDecimal valor,
    String status
) {
}
