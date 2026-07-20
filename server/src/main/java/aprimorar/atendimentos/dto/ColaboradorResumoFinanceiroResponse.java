package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro de um colaborador no período")
public record ColaboradorResumoFinanceiroResponse(
    @NotNull
    @Schema(description = "Quantidade de atendimentos no período", example = "8")
    Long totalAtendimentos,

    @NotNull
    @Schema(description = "Valor total de repasses no período", example = "1820.00")
    BigDecimal valorTotal,

    @NotNull
    @Schema(description = "Valor de repasses pagos no período", example = "1500.00")
    BigDecimal valorPago,

    @NotNull
    @Schema(description = "Valor de repasses pendentes no período", example = "320.00")
    BigDecimal valorPendente
) {
}
