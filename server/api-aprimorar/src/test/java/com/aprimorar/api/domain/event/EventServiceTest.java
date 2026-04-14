package com.aprimorar.api.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.address.Address;
import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.event.dto.EventRequestDTO;
import com.aprimorar.api.domain.event.dto.EventResponseDTO;
import com.aprimorar.api.domain.event.exception.EventScheduleConflictException;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.domain.parent.Parent;
import com.aprimorar.api.domain.student.Student;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.domain.student.repository.StudentRepository;
import com.aprimorar.api.enums.BrazilianStates;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.enums.EventContent;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
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
    private static final Instant EVENT_START = Instant.parse("2026-03-25T13:00:00Z");
    private static final Instant EVENT_END = Instant.parse("2026-03-25T14:00:00Z");
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
        @DisplayName("should create event when input is valid")
        void shouldCreateEventWhenInputIsValid() {
            // Arrange
            EventRequestDTO input = request();
            Event mappedEvent = event();
            Student student = student();
            Employee employee = employee();
            Event savedEvent = eventWithRelations(student, employee);
            EventResponseDTO expected = response();

            when(eventMapper.convertToEntity(input)).thenReturn(mappedEvent);
            when(clock.instant()).thenReturn(CURRENT_TIME);
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(studentRepo.existsByIdAndArchivedAtIsNotNull(STUDENT_ID)).thenReturn(false);
            when(employeeRepo.existsByIdAndArchivedAtIsNotNull(EMPLOYEE_ID)).thenReturn(false);
            when(eventRepo.save(mappedEvent)).thenReturn(savedEvent);
            when(eventMapper.convertToDto(savedEvent)).thenReturn(expected);

            // Act
            EventResponseDTO actual = eventService.createEvent(input);

            // Assert
            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("should throw when student is not found during creation")
        void shouldThrowWhenStudentIsNotFoundDuringCreation() {
            // Arrange
            EventRequestDTO input = request();
            Event mappedEvent = event();

            when(eventMapper.convertToEntity(input)).thenReturn(mappedEvent);
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.empty());

            // Act + Assert
            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessage("Estudante com o ID informado não encontrado no banco de dados");
        }

        @Test
        @DisplayName("should throw when employee is not found during creation")
        void shouldThrowWhenEmployeeIsNotFoundDuringCreation() {
            //Arrange
            EventRequestDTO input = request();
            Event mappedEvent = event();
            Student student = student();

            when(eventMapper.convertToEntity(input)).thenReturn(mappedEvent);
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

            //Act + Assert
            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Colaborador com o ID informado não encontrado no banco de dados");
        }

        @Test
        @DisplayName("should throw when student has conflicting events")
        void shouldThrowWhenStudentHasConflictingEvent() {
            //Arrange
            EventRequestDTO input = request();
            Event mappedEvent = event();
            Student student = student();
            Employee employee = employee();

            when(eventMapper.convertToEntity(input)).thenReturn(mappedEvent);
            when(clock.instant()).thenReturn(CURRENT_TIME);
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(true);

            //Assert + act
            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(EventScheduleConflictException.class)
                .hasMessage("O estudante informado já possui um evento no intervalo");
        }

        @Test
        @DisplayName("should throw when student has conflicting events")
        void shouldThrowWhenEmployeeHasConflictingEvent() {
            //Arrange
            EventRequestDTO input = request();
            Event mappedEvent = event();
            Student student = student();
            Employee employee = employee();

            when(eventMapper.convertToEntity(input)).thenReturn(mappedEvent);
            when(clock.instant()).thenReturn(CURRENT_TIME);
            when(studentRepo.findById(STUDENT_ID)).thenReturn(Optional.of(student));
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));
            when(eventRepo.studentHasConflictingEvent(STUDENT_ID, EVENT_START, EVENT_END, null)).thenReturn(false);
            when(eventRepo.employeeHasConflictingEvent(EMPLOYEE_ID, EVENT_START, EVENT_END, null)).thenReturn(true);

            //Assert + act
            assertThatThrownBy(() -> eventService.createEvent(input))
                .isInstanceOf(EventScheduleConflictException.class)
                .hasMessage("O colaborador informado já possui um evento no intervalo");
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
            EMPLOYEE_ID
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
            CREATED_AT,
            UPDATED_AT
        );
    }

    private static Event event() {
        Event input = new Event();
        input.setId(EVENT_ID);
        input.setTitle("Título temporário");
        input.setDescription("Descrição de teste");
        input.setStartDate(EVENT_START);
        input.setEndDateTime(EVENT_END);
        input.setPayment(BigDecimal.valueOf(80));
        input.setPrice(BigDecimal.valueOf(120));
        input.setContent(EventContent.AULA);
        return input;
    }

    private static Event eventWithRelations(Student student, Employee employee) {
        Event input = event();
        input.setTitle("AULA - Col: Ana Paula - João Silva");
        input.setStudent(student);
        input.setEmployee(employee);
        return input;
    }

    private static Student student() {
        Student input = new Student();
        input.setId(STUDENT_ID);
        input.setName("João Silva");
        input.setContact("61999999999");
        input.setEmail("joao@email.com");
        input.setBirthdate(LocalDate.of(2010, 5, 10));
        input.setCpf("12345678901");
        input.setSchool("Escola Central");
        input.setParent(parent());
        input.setAddress(address());
        return input;
    }

    private static Employee employee() {
        Employee input = new Employee();
        input.setId(EMPLOYEE_ID);
        input.setName("Ana Paula");
        input.setBirthdate(LocalDate.of(1990, 8, 20));
        input.setPix("ana@email.com");
        input.setContact("61988888888");
        input.setCpf("10987654321");
        input.setEmail("ana@email.com");
        input.setDuty(Duty.TEACHER);
        return input;
    }

    private static Parent parent() {
        Parent input = new Parent();
        input.setId(UUID.fromString("dddddddd-dddd-dddd-dddd-dddddddddddd"));
        input.setName("Maria Silva");
        input.setEmail("maria@email.com");
        input.setContact("61977777777");
        input.setCpf("98765432100");
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
