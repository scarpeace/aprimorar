package com.aprimorar.api.mapper;

import org.springframework.stereotype.Component;

import com.aprimorar.api.dto.event.CreateEventDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.entity.Event;

@Component
public class EventMapper {

    public EventResponseDTO toDto(Event entity) {
        if (entity == null) {
            return null;
        }

        return new EventResponseDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getContent() != null ? entity.getContent().name() : null,
                entity.getStartDateTime(),
                entity.getEndDateTime(),
                entity.getPrice(),
                entity.getPayment(),
                entity.getStudent() != null ? entity.getStudent().getId() : null,
                entity.getStudent() != null ? entity.getStudent().getName() : null,
                entity.getEmployee() != null ? entity.getEmployee().getId() : null,
                entity.getEmployee() != null ? entity.getEmployee().getName() : null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public Event toEntity(CreateEventDTO dto) {
        if (dto == null) {
            return null;
        }

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

    public void updateFromDto(CreateEventDTO dto, Event entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.title() != null) {
            entity.setTitle(dto.title());
        }
        if (dto.description() != null) {
            entity.setDescription(dto.description());
        }
        if (dto.startDateTime() != null) {
            entity.setStartDateTime(dto.startDateTime());
        }
        if (dto.endDateTime() != null) {
            entity.setEndDateTime(dto.endDateTime());
        }
        if (dto.price() != null) {
            entity.setPrice(dto.price());
        }
        if (dto.payment() != null) {
            entity.setPayment(dto.payment());
        }
        if (dto.content() != null) {
            entity.setContent(dto.content());
        }
    }
}
