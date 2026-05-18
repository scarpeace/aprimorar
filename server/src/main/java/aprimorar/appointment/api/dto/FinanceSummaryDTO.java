package aprimorar.appointment.api.dto;

import java.math.BigDecimal;

public record FinanceSummaryDTO(
    long totalEvents,
    BigDecimal totalPaid,
    BigDecimal totalPending
) {}
