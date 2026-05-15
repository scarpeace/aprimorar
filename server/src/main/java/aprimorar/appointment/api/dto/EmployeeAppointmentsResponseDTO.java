package aprimorar.appointment.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Agenda paginada e resumo financeiro do colaborador no periodo consultado")
public record EmployeeAppointmentsResponseDTO(
    @Schema(description = "Appointments do colaborador, paginados e filtrados conforme os parametros informados")
    PageDTO<AppointmentResponseDTO> appointments,

    @Schema(description = "Indicadores do colaborador no periodo: total de eventos, total pago e total em aberto")
    EmployeeSummaryDTO summary
) {}
