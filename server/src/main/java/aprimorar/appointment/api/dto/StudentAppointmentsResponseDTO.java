package aprimorar.appointment.api.dto;

import aprimorar.shared.PageDTO;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Agenda paginada e resumo financeiro do aluno no periodo consultado")
public record StudentAppointmentsResponseDTO(
    @Schema(description = "Appointments do aluno, paginados e filtrados conforme os parametros informados")
    PageDTO<AppointmentResponseDTO> appointments,

    @Schema(description = "Indicadores do aluno no periodo: total de eventos, total cobrado e total pendente")
    StudentSummaryDTO summary
) {}
