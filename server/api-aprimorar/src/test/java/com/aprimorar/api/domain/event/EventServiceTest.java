package com.aprimorar.api.domain.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.enums.EventContent;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private static final UUID EVENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID STUDENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID EMPLOYEE_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
    private static final ZoneId APP_ZONE = ZoneId.of("America/Sao_Paulo");
    private static final LocalDateTime EVENT_START = LocalDateTime.of(2026, 3, 25, 10, 0);
    private static final LocalDateTime EVENT_END = LocalDateTime.of(2026, 3, 25, 11, 0);

    @Mock
    private EventRepository eventRepo;

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventService eventService;

    @DisplayName("Command methods")
    private static EventRequestDTO request() {
        return new EventRequestDTO(
                "Evento atualizado",
                "Descrição de teste",
                EVENT_START.atZone(APP_ZONE).toInstant(),
                EVENT_END.atZone(APP_ZONE).toInstant(),
                BigDecimal.valueOf(120),
                BigDecimal.valueOf(80),
                EventContent.AULA,
                STUDENT_ID,
                EMPLOYEE_ID
        );
    }

    private static Event event(String title, Student student, Employee employee) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription("Descrição de teste");
        event.setStartDate(EVENT_START);
        event.setEndDateTime(EVENT_END);
        event.setPrice(BigDecimal.valueOf(120));
        event.setPayment(BigDecimal.valueOf(80));
        event.setContent(EventContent.AULA);
        event.setStudent(student);
        event.setEmployee(employee);
        return event;
    }

    private static Student student(UUID id, String name) {
        Student student = new Student();
        student.setId(id);
        student.setName(name);
        return student;
    }

    private static Employee employee(UUID id, String name) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        return employee;
    }

    private static EventResponseDTO response(Event event, Student student, Employee employee) {
        return new EventResponseDTO(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getContent().name(),
                event.getStartDate().atZone(APP_ZONE).toInstant(),
                event.getEndDateTime().atZone(APP_ZONE).toInstant(),
                event.getPrice(),
                event.getPayment(),
                student.getId(),
                student.getName(),
                employee.getId(),
                employee.getName(),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }
}
