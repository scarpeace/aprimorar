package com.aprimorar.api.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.event.exception.EventScheduleConflictException;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.enums.BrazilianStates;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.enums.EventContent;
import com.aprimorar.api.enums.EventStatus;
import com.aprimorar.api.enums.FinancialStatus;
import com.aprimorar.api.shared.PageDTO;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private static final UUID EVENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID STUDENT_ID = UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID EMPLOYEE_ID = UUID.fromString("cccccccc-cccc-cccc-cccc-cccccccccccc");
    private static final Instant EVENT_START = Instant.parse("2026-03-25T13:00:00Z");
    private static final Instant EVENT_END = Instant.parse("2026-03-25T14:00:00Z");
    private static final Instant UPDATED_EVENT_START = Instant.parse("2026-03-26T13:00:00Z");
    private static final Instant UPDATED_EVENT_END = Instant.parse("2026-03-26T14:00:00Z");
    private static final Instant CREATED_AT = Instant.parse("2026-03-20T10:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-03-21T10:00:00Z");
    private static final Instant CURRENT_TIME = Instant.parse("2026-03-24T10:00:00Z");

    @Mock
    private EventRepository eventRepo;

    @Mock
    private StudentRepository studentRepo;

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private Clock clock;

    @InjectMocks
    private EventService eventService;

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {

        @Test
        @DisplayName("should update income status when event is completed")
        void shouldUpdateIncomeStatusWhenEventIsCompleted() {
            UUID inputId = EVENT_ID;
            FinancialStatus status = FinancialStatus.PAID;
            Event event = event();
            event.setStatus(com.aprimorar.api.enums.EventStatus.COMPLETED);
            EventResponseDTO expected = response();

            when(eventRepo.findById(inputId)).thenReturn(Optional.of(event));
            when(eventMapper.convertToDto(event)).thenReturn(expected);

            EventResponseDTO actual = eventService.settleIncome(inputId, status);

            assertThat(actual).isEqualTo(expected);
            assertThat(event.getIncomeStatus()).isEqualTo(status);
        }

        @Test
        @DisplayName("should throw when updating income status to PAID and event is NOT completed")
        void shouldThrowWhenUpdatingIncomeStatusToPaidAndEventIsNotCompleted() {
            UUID inputId = EVENT_ID;
            FinancialStatus status = FinancialStatus.PAID;
            Event event = event();
            event.setStatus(com.aprimorar.api.enums.EventStatus.SCHEDULED);

            when(eventRepo.findById(inputId)).thenReturn(Optional.of(event));

            assertThatThrownBy(() -> eventService.settleExpense(inputId, status))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Não é possível marcar como pago um evento que não está concluído");
        }

        @Test
        @DisplayName("should update expense status when event is completed")
        void shouldUpdateExpenseStatusWhenEventIsCompleted() {
            UUID inputId = EVENT_ID;
            FinancialStatus status = FinancialStatus.PAID;
            Event event = event();
            event.setStatus(EventStatus.COMPLETED);
            EventResponseDTO expected = response();

            when(eventRepo.findById(inputId)).thenReturn(Optional.of(event));
            when(eventMapper.convertToDto(event)).thenReturn(expected);

            EventResponseDTO actual = eventService.completeEvent(inputId);

            assertThat(actual).isEqualTo(expected);
            assertThat(event.getExpenseStatus()).isEqualTo(status);
        }

        @Test
        @DisplayName("should create event when input is valid")
        void shouldCreateEventWhenInputIsValid() {
            EventRequestDTO input = request();
            Student student = student();
            Employee employee = employee();
            Event savedEvent = eventWithRelations(student, employee);
            EventResponseDTO expected = response();

            when(clock.instant()).thenReturn(CURRENT_TIME);
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(studentRepo.existsByIdAndArchivedAtIsNotNull(STUDENT_ID)).thenReturn(false);
            when(employeeRepo.existsByIdAndArchivedAtIsNotNull(EMPLOYEE_ID)).thenReturn(false);
            when(eventRepo.save(any(Event.class))).thenReturn(savedEvent);
            when(eventMapper.convertToDto(savedEvent)).thenReturn(expected);

            EventResponseDTO actual = eventService.createEvent(input);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("should return the eventsByEmployee page when success")
        void shouldReturnEventsByEmployeePageWhenSuccess() {
            Pageable input = PageRequest.of(0, 2);
            UUID inputEmployeeId = EMPLOYEE_ID;
            Event firstEvent = event();
            Event secondEvent = secondEvent();
            EventResponseDTO expectedFirst = response();
            EventResponseDTO expectedSecond = secondResponse();
            Page<Event> expectedPage = new PageImpl<>(List.of(firstEvent, secondEvent), input, 2);

            when(eventRepo.findAllByEmployeeId(inputEmployeeId, input)).thenReturn(expectedPage);
            when(eventMapper.convertToDto(firstEvent)).thenReturn(expectedFirst);
            when(eventMapper.convertToDto(secondEvent)).thenReturn(expectedSecond);

            PageDTO<EventResponseDTO> actual = eventService.getEventsByEmployeeId(input, inputEmployeeId);

            assertThat(actual.content()).containsExactly(expectedFirst, expectedSecond);
            assertThat(actual.totalElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("should return the event by id when success")
        void shouldReturnEventBasedOnId() {
            UUID input = EVENT_ID;
            Event event = event();
            EventResponseDTO expected = response();

            when(eventRepo.findById(input)).thenReturn(Optional.of(event));
            when(eventMapper.convertToDto(event)).thenReturn(expected);

            EventResponseDTO actual = eventService.findById(input);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("should return the eventsByStudent page when success")
        void shouldReturnEventsByStudentPageWhenSuccess() {
            Pageable input = PageRequest.of(0, 2);
            UUID inputStudentId = STUDENT_ID;
            Event firstEvent = event();
            Event secondEvent = secondEvent();
            EventResponseDTO expectedFirst = response();
            EventResponseDTO expectedSecond = secondResponse();
            Page<Event> expectedPage = new PageImpl<>(List.of(firstEvent, secondEvent), input, 2);

            when(eventRepo.findAllByStudentId(inputStudentId, input)).thenReturn(expectedPage);
            when(eventMapper.convertToDto(firstEvent)).thenReturn(expectedFirst);
            when(eventMapper.convertToDto(secondEvent)).thenReturn(expectedSecond);

            PageDTO<EventResponseDTO> actual = eventService.getEventsByStudentId(input, inputStudentId);

            assertThat(actual.content()).containsExactly(expectedFirst, expectedSecond);
            assertThat(actual.totalElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("should return paged events when search is null")
        void shouldReturnPagedEventsWhenSearchIsNull() {
            Pageable input = PageRequest.of(0, 2);
            Event firstEvent = event();
            Event secondEvent = secondEvent();
            EventResponseDTO expectedFirst = response();
            EventResponseDTO expectedSecond = secondResponse();
            Page<Event> expectedPage = new PageImpl<>(List.of(firstEvent, secondEvent), input, 2);

            when(eventRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(eventMapper.convertToDto(firstEvent)).thenReturn(expectedFirst);
            when(eventMapper.convertToDto(secondEvent)).thenReturn(expectedSecond);

            PageDTO<EventResponseDTO> actual = eventService.getEvents(input, null, null, null, null, null, null);

            assertThat(actual.content()).containsExactly(expectedFirst, expectedSecond);
            assertThat(actual.totalElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("should return paged events when search is informed")
        void shouldReturnPagedEventsWhenSearchIsInformed() {
            Pageable input = PageRequest.of(0, 2);
            String search = " mentoria ";
            Event firstEvent = secondEvent();
            EventResponseDTO expected = secondResponse();
            Page<Event> expectedPage = new PageImpl<>(List.of(firstEvent), input, 1);

            when(eventRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(eventMapper.convertToDto(firstEvent)).thenReturn(expected);

            PageDTO<EventResponseDTO> actual = eventService.getEvents(input, search, null, null, null,null,null);

            assertThat(actual.content()).containsExactly(expected);
            assertThat(actual.totalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("should return paged events when filters are informed")
        void shouldReturnPagedEventsWhenFiltersAreInformed() {
            Pageable input = PageRequest.of(0, 2);
            Instant start = EVENT_START;
            Instant end = EVENT_END;
            com.aprimorar.api.enums.EventStatus status = com.aprimorar.api.enums.EventStatus.SCHEDULED;

            Event firstEvent = event();
            EventResponseDTO expected = response();
            Page<Event> expectedPage = new PageImpl<>(List.of(firstEvent), input, 1);

            when(eventRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(eventMapper.convertToDto(firstEvent)).thenReturn(expected);

            PageDTO<EventResponseDTO> actual = eventService.getEvents(input, null, start, end, status, null, null);

            assertThat(actual.content()).containsExactly(expected);
            assertThat(actual.totalElements()).isEqualTo(1);
        }

        @Test
        @DisplayName("should update event when input is valid")
        void shouldUpdateEventWhenInputIsValid() {
            UUID input = EVENT_ID;
            EventRequestDTO request = updateRequest();
            Student student = student();
            Employee employee = employee();
            Event existingEvent = eventWithRelations(student, employee);
            EventResponseDTO expected = updatedResponse();

            when(eventRepo.findById(input)).thenReturn(Optional.of(existingEvent));
            when(clock.instant()).thenReturn(CURRENT_TIME);
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, UPDATED_EVENT_START, UPDATED_EVENT_END, input)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, UPDATED_EVENT_START, UPDATED_EVENT_END, input)).thenReturn(false);
            when(studentRepo.existsByIdAndArchivedAtIsNotNull(STUDENT_ID)).thenReturn(false);
            when(employeeRepo.existsByIdAndArchivedAtIsNotNull(EMPLOYEE_ID)).thenReturn(false);
            when(eventMapper.convertToDto(existingEvent)).thenReturn(expected);

            EventResponseDTO actual = eventService.updateEvent(input, request);

            assertThat(actual).isEqualTo(expected);
            assertThat(existingEvent.getDescription()).isEqualTo("Descrição atualizada");
            assertThat(existingEvent.getStartDate()).isEqualTo(UPDATED_EVENT_START);
            assertThat(existingEvent.getEndDateTime()).isEqualTo(UPDATED_EVENT_END);
            assertThat(existingEvent.getPayment()).isEqualByComparingTo(BigDecimal.valueOf(90));
            assertThat(existingEvent.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(140));
            assertThat(existingEvent.getContent()).isEqualTo(EventContent.MENTORIA);
            assertThat(existingEvent.getTitle()).isEqualTo("MENTORIA - Col: Ana Paula - João Silva");
            assertThat(existingEvent.getStudent()).isEqualTo(student);
            assertThat(existingEvent.getEmployee()).isEqualTo(employee);
        }

        @Test
        @DisplayName("should throw when updating an event that does not exist")
        void shouldThrowWhenUpdatingEventThatDoesNotExist() {
            UUID input = EVENT_ID;
            EventRequestDTO request = updateRequest();

            when(eventRepo.findById(input)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventService.updateEvent(input, request))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Evento não encontrado no banco de dados");
        }

        @Test
        @DisplayName("should delete event when it exists")
        void shouldDeleteEventWhenItExists() {
            UUID input = EVENT_ID;
            Event expected = eventWithRelations(student(), employee());

            when(eventRepo.findById(input)).thenReturn(Optional.of(expected));

            eventService.deleteEvent(input);

            verify(eventRepo).delete(expected);
        }

        @Test
        @DisplayName("should throw when deleting an event that does not exist")
        void shouldThrowWhenDeletingEventThatDoesNotExist() {
            UUID input = EVENT_ID;

            when(eventRepo.findById(input)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventService.deleteEvent(input))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Evento não encontrado no banco de dados");
        }

        @Test
        @DisplayName("should throw when student is not found during creation")
        void shouldThrowWhenStudentIsNotFoundDuringCreation() {
            EventRequestDTO input = request();
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Estudante com o ID informado não encontrado no banco de dados");
        }

        @Test
        @DisplayName("should throw when employee is not found during creation")
        void shouldThrowWhenEmployeeIsNotFoundDuringCreation() {
            EventRequestDTO input = request();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student()));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Colaborador com o ID informado não encontrado no banco de dados");
        }

        @Test
        @DisplayName("should throw when student has conflicting events")
        void shouldThrowWhenStudentHasConflictingEvent() {
            EventRequestDTO input = request();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student()));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee()));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(true);

            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(EventScheduleConflictException.class)
                .hasMessage("O estudante informado já possui um evento no intervalo");
        }

        @Test
        @DisplayName("should throw when employee has conflicting events")
        void shouldThrowWhenEmployeeHasConflictingEvent() {
            EventRequestDTO input = request();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student()));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee()));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, EVENT_START, EVENT_END, null)).thenReturn(true);

            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(EventScheduleConflictException.class)
                .hasMessage("O colaborador informado já possui um evento no intervalo");
        }

        @Test
        @DisplayName("should throw when creating an event with an archived student")
        void shouldThrowWhenStudentIsArchived() {
            EventRequestDTO input = request();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student()));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee()));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(studentRepo.existsByIdAndArchivedAtIsNotNull(STUDENT_ID)).thenReturn(true);

            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Evento não pode ter estudantes arquivados");
        }

        @Test
        @DisplayName("should throw when creating an event with an archived employee")
        void shouldThrowWhenEmployeeIsArchived() {
            EventRequestDTO input = request();

            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student()));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee()));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(employeeRepo.existsByIdAndArchivedAtIsNotNull(EMPLOYEE_ID)).thenReturn(true);

            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(InvalidEventException.class)
                .hasMessage("Evento não pode ter colaboradores arquivados");
        }

        @Test
        @DisplayName("should throw when event not found by id")
        void shouldThrowWhenEventNotFoundById() {
            when(eventRepo.findById(EVENT_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> eventService.findById(EVENT_ID))
                .isInstanceOf(EventNotFoundException.class)
                .hasMessage("Evento não encontrado no banco de dados");
        }
    }

    private static EventRequestDTO request() {
        return new EventRequestDTO(
            "Descrição de teste",
            EventContent.AULA,
            EVENT_START,
            EVENT_END,
            BigDecimal.valueOf(120),
            BigDecimal.valueOf(80),
            STUDENT_ID,
            EMPLOYEE_ID,
            com.aprimorar.api.enums.EventStatus.SCHEDULED
        );
    }

    private static EventRequestDTO updateRequest() {
        return new EventRequestDTO(
            "Descrição atualizada",
            EventContent.MENTORIA,
            UPDATED_EVENT_START,
            UPDATED_EVENT_END,
            BigDecimal.valueOf(140),
            BigDecimal.valueOf(90),
            STUDENT_ID,
            EMPLOYEE_ID,
            com.aprimorar.api.enums.EventStatus.COMPLETED
        );
    }

    private static EventResponseDTO response() {
        return new EventResponseDTO(
            EVENT_ID,
            "Descrição de teste",
            EventContent.AULA,
            EVENT_START,
            EVENT_END,
            BigDecimal.valueOf(120),
            BigDecimal.valueOf(80),
            STUDENT_ID,
            "João Silva",
            EMPLOYEE_ID,
            "Ana Paula",
            com.aprimorar.api.enums.EventStatus.SCHEDULED,
            FinancialStatus.PENDING,
            FinancialStatus.PENDING,
            CREATED_AT,
            UPDATED_AT
        );
    }

    private static EventResponseDTO secondResponse() {
        return new EventResponseDTO(
            UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"),
            "Descrição de teste 2",
            EventContent.MENTORIA,
            Instant.parse("2026-03-26T13:00:00Z"),
            Instant.parse("2026-03-26T14:00:00Z"),
            BigDecimal.valueOf(130),
            BigDecimal.valueOf(90),
            STUDENT_ID,
            "João Silva",
            EMPLOYEE_ID,
            "Ana Paula",
            com.aprimorar.api.enums.EventStatus.SCHEDULED,
            FinancialStatus.PENDING,
            FinancialStatus.PENDING,
            CREATED_AT,
            UPDATED_AT
        );
    }

    private static EventResponseDTO updatedResponse() {
        return new EventResponseDTO(
            EVENT_ID,
            "Descrição atualizada",
            EventContent.MENTORIA,
            UPDATED_EVENT_START,
            UPDATED_EVENT_END,
            BigDecimal.valueOf(140),
            BigDecimal.valueOf(90),
            STUDENT_ID,
            "João Silva",
            EMPLOYEE_ID,
            "Ana Paula",
            com.aprimorar.api.enums.EventStatus.COMPLETED,
            FinancialStatus.PENDING,
            FinancialStatus.PENDING,
            CREATED_AT,
            UPDATED_AT
        );
    }

    private static Event event() {
        Event input = new Event();
        input.setId(EVENT_ID);
        input.updateDetails(
            "Descrição de teste",
            EVENT_START,
            EVENT_END,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student(),
            employee()
        );
        return input;
    }

    private static Event eventWithRelations(Student student, Employee employee) {
        Event input = new Event();
        input.setId(EVENT_ID);
        input.updateDetails(
            "Descrição de teste",
            EVENT_START,
            EVENT_END,
            BigDecimal.valueOf(80),
            BigDecimal.valueOf(120),
            EventContent.AULA,
            student,
            employee
        );
        return input;
    }

    private static Event secondEvent() {
        Event input = new Event();
        input.setId(UUID.fromString("eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee"));
        input.updateDetails(
            "Descrição de teste 2",
            Instant.parse("2026-03-26T13:00:00Z"),
            Instant.parse("2026-03-26T14:00:00Z"),
            BigDecimal.valueOf(90),
            BigDecimal.valueOf(130),
            EventContent.MENTORIA,
            student(),
            employee()
        );
        return input;
    }

    private static Student student() {
        Student input = new Student(
            "João Silva",
            "61999999999",
            "joao@email.com",
            LocalDate.of(2010, 5, 10),
            "12345678901",
            "Escola Central",
            parent(),
            address()
        );
        input.setId(STUDENT_ID);
        return input;
    }

    private static Employee employee() {
        Employee input = new Employee(
            "Ana Paula",
            LocalDate.of(1990, 8, 20),
            "ana@email.com",
            "61988888888",
            "10987654321",
            "ana@email.com",
            Duty.TEACHER
        );
        input.setId(EMPLOYEE_ID);
        return input;
    }

    private static Parent parent() {
        Parent input = new Parent("Maria Silva", "maria@email.com", "61977777777", "98765432100");
        input.setId(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
        return input;
    }

    private static Address address() {
        Address input = new Address();
        input.setStreet("Rua A");
        input.setDistrict("Centro");
        input.setCity("Brasília");
        input.setState(BrazilianStates.DF);
        input.setZip("70000000");
        input.setComplement("Casa");
        return input;
    }
}
