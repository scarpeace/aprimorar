package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.YearMonth;
import java.util.List;

@Schema(description = "Conteúdo mensal do dashboard de atendimentos")
public record CalendarioMensalAtendimentosResponse(
    @NotNull
    @Schema(description = "Mês de referência", example = "2026-07")
    YearMonth anoMes,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de atendimentos do mês", example = "120")
    Long totalAtendimentos,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de aulas do mês", example = "50")
    Long totalAulas,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de mentorias do mês", example = "20")
    Long totalMentoria,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de terapias do mês", example = "10")
    Long totalTerapia,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de orientações vocacionais do mês", example = "15")
    Long totalOV,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de atendimentos ENEM do mês", example = "8")
    Long totalENEM,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de atendimentos PAS do mês", example = "12")
    Long totalPAS,

    @NotNull
    @PositiveOrZero
    @Schema(description = "Total de outros atendimentos do mês", example = "5")
    Long totalOutros,

    @NotNull
    @Schema(description = "Eventos do mês")
    List<CalendarioAtendimentosResponse> eventos
) {
}
