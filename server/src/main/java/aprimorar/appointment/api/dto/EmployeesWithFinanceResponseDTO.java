package aprimorar.appointment.api.dto;

import java.util.List;

public record EmployeesWithFinanceResponseDTO(
    int page,
    int size,
    long totalElements,
    int totalPages,
    List<EmployeeWithFinanceDTO> content,
    FinanceSummaryDTO financeSummary
) {}
