package com.aprimorar.api.domain.event;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.dto.UpdateEventDTO;

@Component
public class EventMapper {

    public Event convertToEntity(EventRequestDTO dto) {
     
        Event entity = new Event();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setStartDateTime(dto.startDateTime());
        entity.setEndDateTime(dto.endDateTime());
        entity.setPrice(dto.price());
        entity.setPayment(dto.payment());
        entity.setContent(dto.content());
        return entity;
    }

    public EventResponseDTO convertToDto(Event entity) {

        return new EventResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getContent().name(),
                entity.getStartDateTime(),
                entity.getEndDateTime(),
                entity.getPrice(),
                entity.getPayment(),
                entity.getStudentEntity().getId(),
                entity.getStudentEntity().getName(),
                entity.getEmployee().getId(),
                entity.getEmployee().getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Event convertToEntity(UpdateEventDTO updateEventDTO) {
     
        Event entity = new Event();
        entity.setTitle(updateEventDTO.title());
        entity.setDescription(updateEventDTO.description());
        entity.setStartDateTime(updateEventDTO.startDateTime());
        entity.setEndDateTime(updateEventDTO.endDateTime());
        entity.setPrice(updateEventDTO.price());
        entity.setPayment(updateEventDTO.payment());
        entity.setContent(updateEventDTO.content());
        return entity;
    }
}
