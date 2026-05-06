package com.aprimorar.api.domain.employee.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo financeiro e de atendimentos de um colaborador")
public record EmployeeSummaryDTO(

  @Schema(description = "Total de eventos")
  Long totalEvents,

  @Schema(description = "Total pago")
  BigDecimal totalPaid,

  @Schema(description = "Total pendente (não pago)")
  BigDecimal totalUnpaid
) {
}
