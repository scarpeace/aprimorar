package com.aprimorar.api.mapper;

import com.aprimorar.api.dto.event.CreateEventDTO;
import com.aprimorar.api.dto.event.EventResponseDTO;
import com.aprimorar.api.dto.event.UpdateEventDTO;
import com.aprimorar.api.entity.Employee;
import com.aprimorar.api.entity.Event;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.EventContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class EventMapperTest {

    private final EventMapper mapper = new EventMapper();

    @Nested
    @DisplayName("toDto")
    class ToDto {

        @Test
        @DisplayName("maps core fields and participants")
        void mapsEventToDto() {
            Event event = validEventEntity();

            EventResponseDTO dto = mapper.toDto(event);

            assertEquals("Kinematics review", dto.description());
            assertEquals("AULA", dto.content());
            assertEquals("Student Name", dto.studentName());
            assertEquals("Employee Name", dto.employeeName());
            assertEquals(event.getStudent().getId(), dto.studentId());
            assertEquals(event.getEmployee().getId(), dto.employeeId());
        }

        @Test
        @DisplayName("returns null when input is null")
        void returnsNullWhenInputNull() {
            assertNull(mapper.toDto(null));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        @DisplayName("maps create dto fields")
        void mapsCreateDtoToEntity() {
            CreateEventDTO dto = validCreateEventDto();

            Event entity = mapper.toEntity(dto);

            assertEquals(dto.title(), entity.getTitle());
            assertEquals(dto.description(), entity.getDescription());
            assertEquals(dto.startDateTime(), entity.getStartDateTime());
            assertEquals(dto.endDateTime(), entity.getEndDateTime());
            assertEquals(dto.price(), entity.getPrice());
            assertEquals(dto.payment(), entity.getPayment());
            assertEquals(dto.content(), entity.getContent());
        }

        @Test
        @DisplayName("returns null when input is null")
        void returnsNullWhenInputNull() {
            assertNull(mapper.toEntity(null));
        }
    }

    @Test
    @DisplayName("updateFromDto updates only provided scalar fields")
    void updateFromDtoUpdatesOnlyProvidedFields() {
        Event entity = validEventEntity();
        UpdateEventDTO dto = new UpdateEventDTO(
                null,
                "Updated description",
                null,
                null,
                null,
                new BigDecimal("80.00"),
                null,
                null,
                null
        );

        mapper.updateFromDto(dto, entity);

        assertEquals("Physics class", entity.getTitle());
        assertEquals("Updated description", entity.getDescription());
        assertEquals(new BigDecimal("80.00"), entity.getPayment());
        assertEquals(new BigDecimal("100.00"), entity.getPrice());
    }

    private Event validEventEntity() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Physics class");
        event.setDescription("Kinematics review");
        event.setContent(EventContent.AULA);
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

        return event;
    }

    private CreateEventDTO validCreateEventDto() {
        return new CreateEventDTO(
                "Physics class",
                "Kinematics review",
                LocalDateTime.of(2027, 6, 1, 10, 0),
                LocalDateTime.of(2027, 6, 1, 11, 0),
                new BigDecimal("100.00"),
                new BigDecimal("50.00"),
                EventContent.AULA,
                UUID.randomUUID(),
                UUID.randomUUID()
        );
    }
}
