package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.entity.Event;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.EventContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventMapperTest {

    private final EventMapper mapper = new EventMapper();

    @Test
    @DisplayName("Should map description and content correctly in event response")
    void toDto_shouldMapDescriptionAndContent() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Physics class");
        event.setDescription("Kinematics review");
        event.setContent(EventContent.FISICA);
        event.setStartDateTime(LocalDateTime.of(2027, 6, 1, 10, 0));
        event.setEndDateTime(LocalDateTime.of(2027, 6, 1, 11, 0));
        event.setPrice(new BigDecimal("100.00"));
        event.setPayment(new BigDecimal("50.00"));

        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setName("Student Name");
        event.setStudent(student);

        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("Employee Name");
        event.setEmployee(employee);

        EventResponseDTO dto = mapper.toDto(event);

        assertEquals("Kinematics review", dto.description());
        assertEquals("FISICA", dto.content());
    }
}
