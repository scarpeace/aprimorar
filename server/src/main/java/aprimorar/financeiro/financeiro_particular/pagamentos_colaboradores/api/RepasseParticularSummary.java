package aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RepasseParticularSummary(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    String status,
    LocalDateTime createdAt,
    UUID pagamentoId
) {
}
