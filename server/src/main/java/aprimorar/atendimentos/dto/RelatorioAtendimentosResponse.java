package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "Resumo mensal dos atendimentos")
public record RelatorioAtendimentosResponse(
    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de atendimentos", example = "120")
    Long totalAtendimentos,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de aulas", example = "50")
    Long totalAulas,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de mentorias", example = "20")
    Long totalMentoria,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de terapias", example = "10")
    Long totalTerapia,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de orientações vocacionais", example = "15")
    Long totalOV,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de atendimentos de ENEM", example = "8")
    Long totalENEM,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de atendimentos de PAS", example = "12")
    Long totalPAS,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de outros atendimentos", example = "5")
    Long totalOutros
) {
}
