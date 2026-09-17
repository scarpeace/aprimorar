package aprimorar.financeiro.api.repasses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record RepasseResumo(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    String status,
    LocalDateTime dataRepasse,
    String formaPagamento,
    String comprovanteUrl,
    UUID loteId
) {
}
