package aprimorar.atendimentos.internal;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

import aprimorar.atendimentos.api.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.api.dto.AtendimentoResponseDTO;

@Component
public class AtendimentoMapper {

    public Atendimento toEntity(
        AtendimentoRequestDTO dto,
        UUID studentId,
        String studentName,
        UUID employeeId,
        String employeeName,
        Instant now
    ) {
        return new Atendimento(
            dto.description(),
            dto.startDate(),
            dto.duration(),
            dto.payment(),
            dto.price(),
            dto.content(),
            studentId,
            studentName,
            employeeId,
            employeeName,
            now
        );
    }

    public AtendimentoResponseDTO convertToDto(Atendimento atendimento) {
        return new AtendimentoResponseDTO(
            atendimento.getId(),
            atendimento.getDescription(),
            atendimento.getContent(),
            atendimento.getStartDate(),
            atendimento.getEndDate(),
            atendimento.getDuration(),
            atendimento.getPrice(),
            atendimento.getPayment(),
            atendimento.getProfit(),
            atendimento.getStudentId(),
            atendimento.getStudentName(),
            atendimento.getEmployeeId(),
            atendimento.getEmployeeName(),
            atendimento.getEmployeePaymentDate(),
            atendimento.getStudentChargeDate(),
            atendimento.getCreatedAt(),
            atendimento.getUpdatedAt()
        );
    }
}
