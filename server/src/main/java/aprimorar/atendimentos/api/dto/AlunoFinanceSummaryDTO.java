package aprimorar.atendimentos.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AlunoFinanceSummaryDTO(
    UUID studentId,
    String studentName,
    long totalEvents,
    BigDecimal totalCharged,
    BigDecimal totalPending
) {}
