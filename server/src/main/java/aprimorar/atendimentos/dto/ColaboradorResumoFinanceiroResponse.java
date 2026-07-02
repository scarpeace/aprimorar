package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro mensal de um colaborador")
public record ColaboradorResumoFinanceiroResponse(
    @NotNull
    @Schema(description = "Total de repasses pagos no período", example = "1500.00")
    BigDecimal totalPago,

    @NotNull
    @Schema(description = "Total de repasses pendentes no período", example = "320.00")
    BigDecimal totalPendente
) {
}
