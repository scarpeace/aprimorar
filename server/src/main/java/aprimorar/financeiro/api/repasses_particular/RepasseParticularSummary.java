package aprimorar.financeiro.api.repasses_particular;

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
