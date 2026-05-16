package aprimorar.appointment.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record EmployeeFinanceSummaryDTO(
    UUID employeeId,
    String employeeName,
    long totalEvents,
    BigDecimal totalPaid,
    BigDecimal totalPending
) {}
