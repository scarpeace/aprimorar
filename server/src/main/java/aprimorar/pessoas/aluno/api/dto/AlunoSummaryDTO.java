package aprimorar.pessoas.aluno.api.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resumo financeiro e de atendimentos de um aluno")
public record AlunoSummaryDTO(

  @Schema(description = "Total de eventos")
  Long totalEvents,

  @Schema(description = "Total cobrado (pago)")
  BigDecimal totalCharged,

  @Schema(description = "Total pendente (não pago)")
  BigDecimal totalPending
) {
}
