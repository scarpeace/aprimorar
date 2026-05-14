package aprimorar.appointment.api.dto;

import java.time.Instant;
import java.util.List;

public record StudentsFinanceSummaryResponseDTO(
    Instant startDate,
    Instant endDate,
    List<StudentFinanceSummaryDTO> students
) {}
