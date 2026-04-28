package com.aprimorar.api.domain.employee.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo mensal de um funcionário")
public record EmployeeMonthlySummaryDTO(

  @Schema(description = "Total de eventos")
  Long totalEvents,

  @Schema(description = "Total de pagamento")
  BigDecimal totalPayment
) {
}
