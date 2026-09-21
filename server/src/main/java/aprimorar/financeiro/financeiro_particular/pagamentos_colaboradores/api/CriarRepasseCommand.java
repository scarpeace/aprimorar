package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api;

import java.math.BigDecimal;
import java.util.UUID;

public record CriarRepasseCommand(
    Long atendimentoId,
    UUID colaboradorId,
    BigDecimal valor
) {
}
