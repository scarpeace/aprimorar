package aprimorar.registration.student.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo quantitativo dos alunos cadastrados")
public record StudentCountSummaryDTO(
    @Schema(description = "Total de alunos ativos")
    long activeStudents,

    @Schema(description = "Total de alunos cadastrados desde o inicio")
    long totalStudents
) {
}
