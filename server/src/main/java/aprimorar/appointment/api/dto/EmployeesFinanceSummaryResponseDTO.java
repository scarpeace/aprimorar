package aprimorar.appointment.api.dto;

import java.time.Instant;
import java.util.List;

public record EmployeesFinanceSummaryResponseDTO(
    Instant startDate,
    Instant endDate,
    List<EmployeeFinanceSummaryDTO> employees
) {}
