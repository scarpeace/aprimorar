package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponseDTO convertToDto(Event event) {
        return new EventResponseDTO(
            event.getId(),
            event.getDescription(),
            event.getContent(),
            event.getStartDate(),
            event.getEndDateTime(),
            event.getPrice(),
            event.getPayment(),
            event.getStudent().getId(),
            event.getStudent().getName(),
            event.getEmployee().getId(),
            event.getEmployee().getName(),
            event.getCreatedAt(),
            event.getUpdatedAt()
        );
    }
}
