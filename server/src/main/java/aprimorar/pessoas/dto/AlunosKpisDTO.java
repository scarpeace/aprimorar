package aprimorar.pessoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Formato de payload para obter opções de colaboradores")
public record AlunosKpisDTO(
        @Schema(description = "ID do funcionário")
        long totalAlunos,
        @Schema(description = "Total de colaboradores ativos")
        long totalAlunosAtivos
) {
}
