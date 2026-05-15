package aprimorar.appointment.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro consolidado baseado em appointments e despesas gerais")
public record AppointmentFinanceSummaryDTO(
    BigDecimal totalStudentCharged,
    BigDecimal totalStudentPending,
    BigDecimal totalEmployeePaid,
    BigDecimal totalEmployeePending,
    BigDecimal totalGeneralExpenses,
    BigDecimal balance
) {}
