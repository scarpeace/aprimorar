package aprimorar.appointment.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record StudentFinanceSummaryDTO(
    UUID studentId,
    String studentName,
    long totalEvents,
    BigDecimal totalCharged,
    BigDecimal totalPending
) {}
