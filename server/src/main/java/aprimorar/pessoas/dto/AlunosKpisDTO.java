package aprimorar.pessoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Resumo de alunos cadastrados")
public record AlunosKpisDTO(
        @NotNull
        @PositiveOrZero(message = "O total não pode ser negativo")
        @Schema(description = "Total de alunos cadastrados", example = "120")
        long totalAlunos,
        @NotNull
        @PositiveOrZero(message = "O total não pode ser negativo")
        @Schema(description = "Total de alunos ativos", example = "98")
        long totalAlunosAtivos
) {
}
