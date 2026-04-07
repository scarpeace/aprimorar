package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event convertToEntity(EventRequestDTO request) {
        Event event = new Event();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartDate(request.startDate());
        event.setEndDateTime(request.endDate());
        event.setPayment(request.payment());
        event.setPrice(request.price());
        event.setContent(request.content());
        return event;
    }

    public EventResponseDTO convertToDto(Event event) {
        return new EventResponseDTO(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getContent(),
            event.getStartDate(),
            event.getEndDateTime(),
            event.getPayment(),
            event.getPrice(),
            event.getStudent().getId(),
            event.getStudent().getName(),
            event.getEmployee().getId(),
            event.getEmployee().getName(),
            event.getCreatedAt(),
            event.getUpdatedAt()
        );
    }
}
