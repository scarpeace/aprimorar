package aprimorar.atendimentos.api.dto;


public record AtendimentosContentReportDTO (
    long totalAulas,
    long totalMentoria,
    long totalTerapia,
    long totalOV,
    long totalENEM,
    long totalPAS,
    long totalOutros
) {

}
