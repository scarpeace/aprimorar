package aprimorar.appointment.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Appointments paginados do colaborador com resumo do periodo")
public record EmployeeAppointmentsDTO(
    @Schema(description = "Lista paginada de appointments do colaborador")
    PageDTO<AppointmentResponseDTO> appointments,

    @Schema(description = "Total de eventos no periodo selecionado")
    Long totalEvents,

    @Schema(description = "Total pago no periodo selecionado")
    BigDecimal totalPaid,

    @Schema(description = "Total pendente no periodo selecionado")
    BigDecimal totalUnpaid
) {}
