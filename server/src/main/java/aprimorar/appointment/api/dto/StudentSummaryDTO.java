package aprimorar.appointment.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Resumo financeiro e quantitativo do aluno no periodo")
public record StudentSummaryDTO(
    @Schema(description = "Total de eventos no periodo selecionado")
    Long totalEvents,

    @Schema(description = "Total cobrado no periodo selecionado")
    BigDecimal totalCharged,

    @Schema(description = "Total pendente no periodo selecionado")
    BigDecimal totalPending
) {}
