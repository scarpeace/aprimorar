package aprimorar.financeiro.api.cobrancas;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CobrancaResumo(
    Long id,
    Long atendimentoId,
    BigDecimal valor,
    String status,
    LocalDateTime dataPagamento,
    String formaPagamento,
    String comprovanteUrl,
    UUID loteId
) {
}
