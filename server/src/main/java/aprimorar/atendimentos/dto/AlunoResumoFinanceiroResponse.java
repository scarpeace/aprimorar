package aprimorar.atendimentos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro mensal de um aluno")
public record AlunoResumoFinanceiroResponse(
    @NotNull
    @Schema(description = "Total de pagamentos efetuados no período", example = "1500.00")
    BigDecimal totalPago,

    @NotNull
    @Schema(description = "Total de pagamentos pendentes no período", example = "320.00")
    BigDecimal totalPendente
) {
}
