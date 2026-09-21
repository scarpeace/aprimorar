package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api;

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
