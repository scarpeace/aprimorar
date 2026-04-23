package com.aprimorar.api.domain.finance.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo financeiro consolidado")
public record FinanceSummaryDTO(
    @Schema(description = "Total de receita recebida", example = "5000.00")
    BigDecimal totalIncome,

    @Schema(description = "Total de receita pendente", example = "1200.00")
    BigDecimal totalIncomePending,

    @Schema(description = "Total de despesa operacional paga (colaboradores)", example = "3000.00")
    BigDecimal totalExpenseTeacher,

    @Schema(description = "Total de despesa operacional pendente (colaboradores)", example = "800.00")
    BigDecimal totalExpenseTeacherPending,

    @Schema(description = "Total de despesas gerais", example = "500.00")
    BigDecimal totalGeneralExpenses,

    @Schema(description = "Saldo final (Receita Paga - Despesa Operacional Paga - Despesas Gerais)", example = "1500.00")
    BigDecimal balance
) {}
