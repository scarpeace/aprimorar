package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Relatório de atendimentos por conteúdo.")
public record AtendimentosContentReportDTO (

    @Schema(description = "Total de atendimentos tipo AULAS.")
    long totalAulas,
    @Schema(description = "Total de atendimentos tipo MENTORIA.")
    long totalMentoria,
    @Schema(description = "Total de atendimentos tipo TERAPIA.")
    long totalTerapia,
    @Schema(description = "Total de atendimentos tipo O.V.")
    long totalOV,
    @Schema(description = "Total de atendimentos tipo ENEM.")
    long totalENEM,
    @Schema(description = "Total de atendimentos tipo PAS.")
    long totalPAS,
    @Schema(description = "Total de atendimentos tipo OUTROS.")
    long totalOutros
) {

}
