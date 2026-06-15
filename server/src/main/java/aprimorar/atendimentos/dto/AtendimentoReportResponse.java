package aprimorar.atendimentos.dto;


public record AtendimentoReportResponse(
    Long totalAtendimentos,

    Long totalAulas,
    Double procentagemAulas,

    Long totalMentoria,
    Double procentagemMentoria,

    Long totalTerapia,
    Double procentagemTerapia,

    Long totalOV,
    Double procentagemOV,

    Long totalENEM,
    Double procentagemENEM,

    Long totalPAS,
    Double procentagemPAS,

    Long totalOutros,
    Double procentagemOutros
) {
}
