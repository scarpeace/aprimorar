package aprimorar.atendimentos.internal;

import org.springframework.stereotype.Component;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;

@Component
public class AtendimentoMapper {

    public AtendimentoResponseDTO convertToDto(Atendimento appointment) {
        return new AtendimentoResponseDTO(
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
