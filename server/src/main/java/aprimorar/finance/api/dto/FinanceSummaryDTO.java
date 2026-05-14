package aprimorar.finance.api.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo financeiro consolidado")
public record FinanceSummaryDTO(
    @Schema(description = "Total pago pelos alunos", example = "5000.00")
    BigDecimal totalStudentCharged,

    @Schema(description = "Total pendente dos alunos", example = "1200.00")
    BigDecimal totalStudentPending,

    @Schema(description = "Total pago aos colaboradores", example = "3000.00")
    BigDecimal totalEmployeePaid,

    @Schema(description = "Total pendente aos colaboradores", example = "800.00")
    BigDecimal totalEmployeePending,

    @Schema(description = "Total de despesas gerais", example = "500.00")
    BigDecimal totalGeneralExpenses,

    @Schema(description = "Saldo final (Receita Paga - Despesa Operacional Paga - Despesas Gerais)", example = "1500.00")
    BigDecimal balance
) {}
