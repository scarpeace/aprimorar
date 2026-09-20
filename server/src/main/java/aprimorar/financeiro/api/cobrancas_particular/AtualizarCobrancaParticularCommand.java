package aprimorar.financeiro.api.cobrancas_particular;

import java.math.BigDecimal;
import java.util.UUID;

public record AtualizarCobrancaParticularCommand(
    Long atendimentoId,
    UUID alunoId,
    BigDecimal valor
) {
}
