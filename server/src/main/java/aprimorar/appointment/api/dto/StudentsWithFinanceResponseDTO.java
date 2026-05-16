package aprimorar.appointment.api.dto;

import java.util.List;

public record StudentsWithFinanceResponseDTO(
    int page,
    int size,
    long totalElements,
    int totalPages,
    List<StudentWithFinanceDTO> content,
    StudentSummaryDTO financeSummary
) {}
