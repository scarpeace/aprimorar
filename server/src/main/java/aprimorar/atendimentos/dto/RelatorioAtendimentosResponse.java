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
    @PositiveOrZero(message = "A porcentagem não pode ser negativa")
    @Schema(description = "Percentual de aulas", example = "41.6")
    Double procentagemAulas,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de mentorias", example = "20")
    Long totalMentoria,

    @NotNull
    @PositiveOrZero(message = "A porcentagem não pode ser negativa")
    @Schema(description = "Percentual de mentorias", example = "16.6")
    Double procentagemMentoria,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de terapias", example = "10")
    Long totalTerapia,

    @NotNull
    @PositiveOrZero(message = "A porcentagem não pode ser negativa")
    @Schema(description = "Percentual de terapias", example = "8.3")
    Double procentagemTerapia,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de orientações vocacionais", example = "15")
    Long totalOV,

    @NotNull
    @PositiveOrZero(message = "A porcentagem não pode ser negativa")
    @Schema(description = "Percentual de orientações vocacionais", example = "12.5")
    Double procentagemOV,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de atendimentos de ENEM", example = "8")
    Long totalENEM,

    @NotNull
    @PositiveOrZero(message = "A porcentagem não pode ser negativa")
    @Schema(description = "Percentual de atendimentos de ENEM", example = "6.6")
    Double procentagemENEM,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de atendimentos de PAS", example = "12")
    Long totalPAS,

    @NotNull
    @PositiveOrZero(message = "A porcentagem não pode ser negativa")
    @Schema(description = "Percentual de atendimentos de PAS", example = "10.0")
    Double procentagemPAS,

    @NotNull
    @PositiveOrZero(message = "O total não pode ser negativo")
    @Schema(description = "Total de outros atendimentos", example = "5")
    Long totalOutros,

    @NotNull
    @PositiveOrZero(message = "A porcentagem não pode ser negativa")
    @Schema(description = "Percentual de outros atendimentos", example = "4.1")
    Double procentagemOutros
) {
}
