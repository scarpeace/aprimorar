package aprimorar.financeiro.api.financeiro_aluno;

import aprimorar.financeiro.api.financeiro_aluno.TipoOrigemCobrancaAluno;
import java.math.BigDecimal;
import java.util.UUID;

public record AtualizarCobrancaAlunoCommand(
    Long origemId,
    TipoOrigemCobrancaAluno origemTipo,
    UUID alunoId,
    BigDecimal valorTotal
) {
}
