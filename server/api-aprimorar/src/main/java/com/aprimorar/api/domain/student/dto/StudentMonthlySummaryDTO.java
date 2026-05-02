package com.aprimorar.api.domain.student.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo mensal de um aluno")
public record StudentMonthlySummaryDTO(

  @Schema(description = "Total de eventos")
  Long totalEventsInPeriod,

  @Schema(description = "Total cobrado (pago)")
  BigDecimal totalChargedInPeriod,

  @Schema(description = "Total pendente (não pago)")
  BigDecimal totalPendingInPeriod
) {
}
