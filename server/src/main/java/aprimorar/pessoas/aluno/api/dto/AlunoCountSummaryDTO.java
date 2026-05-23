package aprimorar.pessoas.aluno.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo quantitativo dos alunos cadastrados")
public record AlunoCountSummaryDTO(
    @Schema(description = "Total de alunos ativos")
    long activeStudents,

    @Schema(description = "Total de alunos cadastrados desde o inicio")
    long totalStudents
) {
}
