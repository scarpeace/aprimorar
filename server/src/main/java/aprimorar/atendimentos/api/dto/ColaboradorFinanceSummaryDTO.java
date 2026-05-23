package aprimorar.atendimentos.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ColaboradorFinanceSummaryDTO(
    UUID employeeId,
    String employeeName,
    long totalEvents,
    BigDecimal totalPaid,
    BigDecimal totalPending
) {}
