package aprimorar.event.internal;

import org.springframework.stereotype.Component;
import aprimorar.event.api.dto.EventResponseDTO;

@Component
public class EventMapper {

    public EventResponseDTO convertToDto(Event event) {
        return new EventResponseDTO(
            event.getId(),
            event.getDescription(),
            event.getContent(),
            event.getStartDate(),
            event.getEndDateTime(),
            event.getDuration(),
            event.getPrice(),
            event.getPayment(),
            event.getProfit(),
            event.getStudentId(),
            event.getEmployeeId(),
            event.getEmployeePaymentDate(),
            event.getStudentChargeDate(),
            event.getCreatedAt(),
            event.getUpdatedAt()
        );
    }
}
