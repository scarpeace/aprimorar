package aprimorar.atendimentos.api.dto;

import java.util.List;

public record AlunosWithFinanceResponseDTO(
    int page,
    int size,
    long totalElements,
    int totalPages,
    List<AlunoWithFinanceDTO> content,
    AlunoSummaryDTO financeSummary
) {}
