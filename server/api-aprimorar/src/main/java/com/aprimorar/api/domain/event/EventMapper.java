package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private static final ZoneId APP_ZONE = ZoneId.of("America/Sao_Paulo");

    public Event convertToEntity(EventRequestDTO request) {
        Event event = new Event();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartDate(toLocalDateTime(request.startDate()));
        event.setEndDateTime(toLocalDateTime(request.endDate()));
        event.setPrice(request.price());
        event.setPayment(request.payment());
        event.setContent(request.content());
        return event;
    }

    public LocalDateTime toLocalDateTime(Instant value) {
        return value.atZone(APP_ZONE).toLocalDateTime();
    }

    public Instant toInstant(LocalDateTime value) {
        return value.atZone(APP_ZONE).toInstant();
    }

    public EventResponseDTO convertToDto(Event event) {
        return new EventResponseDTO(
            event.getId(),
            event.getTitle(),
            event.getDescription(),
            event.getContent().name(),
            toInstant(event.getStartDate()),
            toInstant(event.getEndDateTime()),
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
