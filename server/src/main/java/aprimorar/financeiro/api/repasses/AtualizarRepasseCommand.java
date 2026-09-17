package aprimorar.financeiro.api.repasses;

import java.math.BigDecimal;
import java.util.UUID;

public record AtualizarRepasseCommand(
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor
) {
}
