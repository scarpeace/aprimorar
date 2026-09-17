package aprimorar.financeiro.api.repasses;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarRepasseCommand(
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor
) {
}
