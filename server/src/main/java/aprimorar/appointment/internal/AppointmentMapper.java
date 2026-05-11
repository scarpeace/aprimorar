package aprimorar.appointment.internal;

import org.springframework.stereotype.Component;
import aprimorar.appointment.api.dto.AppointmentResponseDTO;

@Component
public class AppointmentMapper {

    public AppointmentResponseDTO convertToDto(Appointment appointment) {
        return new AppointmentResponseDTO(
            appointment.getId(),
            appointment.getDescription(),
            appointment.getContent(),
            appointment.getStartDate(),
            appointment.getEndDate(),
            appointment.getDuration(),
            appointment.getPrice(),
            appointment.getPayment(),
            appointment.getProfit(),
            appointment.getStudentId(),
            appointment.getStudentName(),
            appointment.getEmployeeId(),
            appointment.getEmployeeName(),
            appointment.getEmployeePaymentDate(),
            appointment.getStudentChargeDate(),
            appointment.getCreatedAt(),
            appointment.getUpdatedAt()
        );
    }
}
