package com.aprimorar.api.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.EmployeeRepository;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.domain.student.StudentRepository;
import com.aprimorar.api.enums.EventContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private static final UUID EVENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID STUDENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID EMPLOYEE_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");

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

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {

        @Test
        @DisplayName("should update existing event ignoring current event in conflict validation")
        void shouldUpdateExistingEventIgnoringCurrentEventInConflictValidation() {
            EventRequestDTO input = request();
            Event mappedEvent = event("Evento atualizado", student(STUDENT_ID, null), employee(EMPLOYEE_ID, null));
            Event existingEvent = event("Evento antigo", student(STUDENT_ID, "Aluno Antigo"), employee(EMPLOYEE_ID, "Colaborador Antigo"));
            Student resolvedStudent = student(STUDENT_ID, "Aluno Teste");
            Employee resolvedEmployee = employee(EMPLOYEE_ID, "Colaborador Teste");
            existingEvent.setId(EVENT_ID);
            EventResponseDTO expected = response(existingEvent, resolvedStudent, resolvedEmployee);

            when(eventMapper.convertToEntity(input)).thenReturn(mappedEvent);
            when(eventRepo.findById(EVENT_ID)).thenReturn(Optional.of(existingEvent));
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(resolvedStudent));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(resolvedEmployee));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, mappedEvent.getStartDate(), mappedEvent.getEndDateTime(), EVENT_ID)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, mappedEvent.getStartDate(), mappedEvent.getEndDateTime(), EVENT_ID)).thenReturn(false);
            when(eventMapper.convertToDto(existingEvent)).thenReturn(expected);

            EventResponseDTO actual = eventService.updateEvent(EVENT_ID, input);

            assertThat(actual).isEqualTo(expected);
            assertThat(existingEvent.getTitle()).isEqualTo("Evento atualizado");
            assertThat(existingEvent.getStudent()).isEqualTo(resolvedStudent);
            assertThat(existingEvent.getEmployee()).isEqualTo(resolvedEmployee);
            verify(eventRepo).findById(EVENT_ID);
            verify(studentRepo).findById(STUDENT_ID);
            verify(employeeRepo).findById(EMPLOYEE_ID);
            verify(eventRepo).studentHasConflictingEvent(STUDENT_ID, mappedEvent.getStartDate(), mappedEvent.getEndDateTime(), EVENT_ID);
            verify(eventRepo).employeeHasConflictingEvent(EMPLOYEE_ID, mappedEvent.getStartDate(), mappedEvent.getEndDateTime(), EVENT_ID);
            verify(eventRepo, never()).save(any(Event.class));
        }
    }

    private static EventRequestDTO request() {
        return new EventRequestDTO(
                "Evento atualizado",
                "Descrição de teste",
                LocalDateTime.of(2026, 3, 20, 10, 0),
                LocalDateTime.of(2026, 3, 20, 11, 0),
                BigDecimal.valueOf(120),
                BigDecimal.valueOf(80),
                EventContent.AULA,
                student(STUDENT_ID, null),
                employee(EMPLOYEE_ID, null)
        );
    }

    private static Event event(String title, Student student, Employee employee) {
        Event event = new Event();
        event.setTitle(title);
        event.setDescription("Descrição de teste");
        event.setStartDate(LocalDateTime.of(2026, 3, 20, 10, 0));
        event.setEndDateTime(LocalDateTime.of(2026, 3, 20, 11, 0));
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
                event.getStartDate(),
                event.getEndDateTime(),
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
