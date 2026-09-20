package aprimorar.financeiro.api.cobrancas_particular;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CobrancaParticularSummary(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    String status,
    LocalDateTime createdAt,
    UUID recebimentoId
) {
}
