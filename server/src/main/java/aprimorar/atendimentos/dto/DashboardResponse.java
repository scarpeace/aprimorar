package aprimorar.atendimentos.dto;

import java.util.List;

public record DashboardResponse(
    Long totalAulas,
    Long totalMentoria,
    Long totalTerapia,
    Long totalOV,
    Long totalENEM,
    Long totalPAS,
    Long totalOutros,
    List<AtendimentoCalendarioResponse> calendarioAtendimentos
) {
}
