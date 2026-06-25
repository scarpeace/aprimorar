package aprimorar.pessoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Resumo de colaboradores cadastrados")
public record ColaboradoresKpisDTO(
        @NotNull
        @PositiveOrZero(message = "O total não pode ser negativo")
        @Schema(description = "Total de colaboradores cadastrados", example = "30")
        long totalColaboradores,
        @NotNull
        @PositiveOrZero(message = "O total não pode ser negativo")
        @Schema(description = "Total de colaboradores ativos", example = "25")
        long totalColaboradoresAtivos
) {
}
