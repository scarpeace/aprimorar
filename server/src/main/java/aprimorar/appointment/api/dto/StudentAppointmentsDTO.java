package aprimorar.appointment.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Appointments paginados do aluno com resumo do periodo")
public record StudentAppointmentsDTO(
    @Schema(description = "Lista paginada de appointments do aluno")
    PageDTO<AppointmentResponseDTO> appointments,

    @Schema(description = "Total de eventos no periodo selecionado")
    Long totalEvents,

    @Schema(description = "Total cobrado no periodo selecionado")
    BigDecimal totalCharged,

    @Schema(description = "Total pendente no periodo selecionado")
    BigDecimal totalPending
) {}
