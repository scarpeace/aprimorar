package aprimorar.atendimentos.api.dto;

import java.time.Instant;
import java.util.List;

public record AlunosFinanceSummaryResponseDTO(
    Instant startDate,
    Instant endDate,
    List<AlunoFinanceSummaryDTO> students
) {}
