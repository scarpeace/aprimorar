package aprimorar.atendimentos.api.dto;

import java.util.List;

public record ColaboradoresWithFinanceResponseDTO(
    int page,
    int size,
    long totalElements,
    int totalPages,
    List<ColaboradorWithFinanceDTO> content,
    FinanceSummaryDTO financeSummary
) {}
