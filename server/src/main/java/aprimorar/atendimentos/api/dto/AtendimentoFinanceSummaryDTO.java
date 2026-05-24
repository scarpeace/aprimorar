package aprimorar.atendimentos.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro consolidado baseado em atendimentos e despesas gerais")
public record AtendimentoFinanceSummaryDTO(
    BigDecimal totalStudentCharged,
    BigDecimal totalStudentPending,
    BigDecimal totalEmployeePaid,
    BigDecimal totalEmployeePending,
    BigDecimal totalGeneralExpenses,
    BigDecimal balance
) {}
