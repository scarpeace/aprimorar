package aprimorar.pessoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo de alunos cadastrados")
public record AlunosKpisDTO(
        @Schema(description = "Total de alunos cadastrados")
        long totalAlunos,
        @Schema(description = "Total de alunos ativos")
        long totalAlunosAtivos
) {
}
