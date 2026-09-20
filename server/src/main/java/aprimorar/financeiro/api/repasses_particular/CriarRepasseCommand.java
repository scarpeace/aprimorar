package aprimorar.financeiro.api.repasses_particular;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarRepasseCommand(
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor
) {
}
