package aprimorar.atendimentos.api.dto;

import java.time.Instant;
import java.util.List;

public record ColaboradoresFinanceSummaryResponseDTO(
    Instant startDate,
    Instant endDate,
    List<ColaboradorFinanceSummaryDTO> employees
) {}
