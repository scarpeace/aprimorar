package aprimorar.financeiro.api.cobrancas;

import java.math.BigDecimal;
import java.util.UUID;

public record AtualizarCobrancaCommand(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {
}
