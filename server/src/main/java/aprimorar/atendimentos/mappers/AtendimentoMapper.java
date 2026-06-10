package aprimorar.atendimentos.mappers;

import org.springframework.stereotype.Component;

import java.time.Instant;

import aprimorar.atendimentos.dto.AtendimentoRequestDTO;
import aprimorar.atendimentos.dto.AtendimentoResponseDTO;
import aprimorar.atendimentos.domain.Atendimento;

@Component
public class AtendimentoMapper {

    public Atendimento toEntity(
        AtendimentoRequestDTO dto,
        String studentName,
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
            dto.studentId(),
            studentName,
            dto.employeeId(),
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
