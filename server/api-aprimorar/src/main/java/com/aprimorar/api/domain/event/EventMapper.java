package com.aprimorar.api.domain.event;

import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.dto.UpdateEventDTO;

@Component
public class EventMapper {

    public EventEntity convertToEntity(EventRequestDTO dto) {
     
        EventEntity entity = new EventEntity();
        entity.setTitle(dto.title());
        entity.setDescription(dto.description());
        entity.setStartDateTime(dto.startDateTime());
        entity.setEndDateTime(dto.endDateTime());
        entity.setPrice(dto.price());
        entity.setPayment(dto.payment());
        entity.setContent(dto.content());
        return entity;
    }

    public EventResponseDTO convertToDto(EventEntity entity) {

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
                entity.getEmployeeEntity().getId(),
                entity.getEmployeeEntity().getName(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public EventEntity convertToEntity(UpdateEventDTO updateEventDTO) {
     
        EventEntity entity = new EventEntity();
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
