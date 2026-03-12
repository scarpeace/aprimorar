package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.event.command.EventCommand;
import com.aprimorar.api.shared.MapperUtils;
import org.springframework.stereotype.Component;

import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;

@Component
public class EventMapper {

    public EventCommand convertToCommand(EventRequestDTO dto) {
        return new EventCommand(
                dto.title().trim(),
                dto.description(),
                dto.startDateTime(),
                dto.endDateTime(),
                dto.price(),
                dto.payment(),
                dto.content()
        );
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


}
