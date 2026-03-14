package com.aprimorar.api.domain.event;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.event.command.EventCommand;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.student.Student;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event convertToEntity(EventRequestDTO request) {
        Event event = new Event();
        event.setTitle(request.title());
        event.setDescription(request.description());
        event.setStartDate(request.startDate());
        event.setEndDateTime(request.endDate());
        event.setPrice(request.price());
        event.setPayment(request.payment());
        event.setContent(request.content());
        event.setStudent(request.student());
        event.setEmployee(request.employee());
        return event;
    }

    public EventResponseDTO convertToDto(Event event) {
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getContent().name(),
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

