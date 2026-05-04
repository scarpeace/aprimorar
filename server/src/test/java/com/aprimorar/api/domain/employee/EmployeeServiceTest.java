package com.aprimorar.api.domain.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.auth.repository.UserRepository;
import com.aprimorar.api.domain.employee.dto.EmployeeSummaryDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeOptionsDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeRequestDTO;
import com.aprimorar.api.domain.employee.dto.EmployeeResponseDTO;
import com.aprimorar.api.domain.employee.exception.EmployeeAlreadyExistsException;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.repository.EmployeeRepository;
import com.aprimorar.api.domain.event.repository.EventRepository;
import com.aprimorar.api.enums.Duty;
import com.aprimorar.api.shared.PageDTO;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    private static final UUID EMPLOYEE_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID SECOND_EMPLOYEE_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID MISSING_EMPLOYEE_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID GHOST_EMPLOYEE_ID = UUID.fromString("00000000-0000-4000-8000-000000000001");
    private static final Instant CREATED_AT = Instant.parse("2026-01-05T08:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-01-12T12:30:00Z");
    private static final Instant ARCHIVED_AT = Instant.parse("2026-01-10T10:15:30Z");

    @Mock
    private EmployeeRepository employeeRepo;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EventRepository eventRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private Clock clock;

    @InjectMocks
    private EmployeeService employeeService;

    @Nested
    @DisplayName("Command methods")
    class CommandMethods {

        @Test
        @DisplayName("should create employee when input is valid")
        void shouldCreateEmployeeWhenInputIsValid() {
            EmployeeRequestDTO input = request();
            Employee savedEmployee = employee();
            EmployeeResponseDTO expected = response();

            when(employeeRepo.existsByCpf("10987654321")).thenReturn(false);
            when(employeeRepo.existsByEmail("ana@email.com")).thenReturn(false);
            when(employeeRepo.save(any(Employee.class))).thenReturn(savedEmployee);
            when(employeeMapper.convertToDto(savedEmployee)).thenReturn(expected);

            EmployeeResponseDTO actual = employeeService.createEmployee(input);

            assertThat(actual).isEqualTo(expected);
            verify(employeeRepo).existsByCpf("10987654321");
            verify(employeeRepo).existsByEmail("ana@email.com");
            verify(employeeRepo).save(any(Employee.class));
        }

        @Test
        @DisplayName("should throw when creating employee with duplicated cpf")
        void shouldThrowWhenCreatingEmployeeWithDuplicatedCpf() {
            when(employeeRepo.existsByCpf("10987654321")).thenReturn(true);

            assertThatThrownBy(() -> employeeService.createEmployee(request()))
                .isInstanceOf(EmployeeAlreadyExistsException.class)
                .hasMessage("Colaborador com o CPF informado já cadastrado no banco de dados");

            verify(employeeRepo).existsByCpf("10987654321");
            verify(employeeRepo, never()).existsByEmail(any());
            verify(employeeRepo, never()).save(any());
        }

        @Test
        @DisplayName("should throw when creating employee with duplicated email")
        void shouldThrowWhenCreatingEmployeeWithDuplicatedEmail() {
            when(employeeRepo.existsByCpf("10987654321")).thenReturn(false);
            when(employeeRepo.existsByEmail("ana@email.com")).thenReturn(true);

            assertThatThrownBy(() -> employeeService.createEmployee(request()))
                .isInstanceOf(EmployeeAlreadyExistsException.class)
                .hasMessage("Colaborador com o Email informado já cadastrado no banco de dados");

            verify(employeeRepo).existsByCpf("10987654321");
            verify(employeeRepo).existsByEmail("ana@email.com");
            verify(employeeRepo, never()).save(any());
        }

        @Test
        @DisplayName("should update employee when input is valid")
        void shouldUpdateEmployeeWhenInputIsValid() {
            Employee existingEmployee = employee();
            EmployeeRequestDTO input = updatedRequest();
            EmployeeResponseDTO expected = updatedResponse();

            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(existingEmployee));
            when(employeeRepo.existsByEmailAndIdNot("ana.beatriz@email.com", EMPLOYEE_ID)).thenReturn(false);
            when(employeeMapper.convertToDto(existingEmployee)).thenReturn(expected);

            EmployeeResponseDTO actual = employeeService.updateEmployee(EMPLOYEE_ID, input);

            assertThat(actual).isEqualTo(expected);
            assertThat(existingEmployee.getName()).isEqualTo("Ana Beatriz");
            assertThat(existingEmployee.getBirthdate()).isEqualTo(LocalDate.of(1991, 1, 10));
            assertThat(existingEmployee.getPix()).isEqualTo("ana.pix");
            assertThat(existingEmployee.getContact()).isEqualTo("11999998888");
            assertThat(existingEmployee.getEmail()).isEqualTo("ana.beatriz@email.com");
            assertThat(existingEmployee.getDuty()).isEqualTo(Duty.ADM);
            assertThat(existingEmployee.getCpf()).isEqualTo("10987654321");
            verify(employeeRepo).existsByEmailAndIdNot("ana.beatriz@email.com", EMPLOYEE_ID);
        }

        @Test
        @DisplayName("should throw when updating employee with duplicated email")
        void shouldThrowWhenUpdatingEmployeeWithDuplicatedEmail() {
            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee()));
            when(employeeRepo.existsByEmailAndIdNot("ana.beatriz@email.com", EMPLOYEE_ID)).thenReturn(true);

            assertThatThrownBy(() -> employeeService.updateEmployee(EMPLOYEE_ID, updatedRequest()))
                .isInstanceOf(EmployeeAlreadyExistsException.class)
                .hasMessage("Colaborador com o Email informado já cadastrado no banco de dados");

            verify(employeeMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should throw when updating employee that does not exist")
        void shouldThrowWhenUpdatingEmployeeThatDoesNotExist() {
            when(employeeRepo.findById(MISSING_EMPLOYEE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> employeeService.updateEmployee(MISSING_EMPLOYEE_ID, updatedRequest()))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Colaborador não encontrado no Banco de Dados");

            verify(employeeMapper, never()).convertToDto(any());
        }

        @Test
        @DisplayName("should archive employee by setting archivedAt")
        void shouldArchiveEmployeeBySettingArchivedAt() {
            Employee existingEmployee = employee();

            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(existingEmployee));

            employeeService.archiveEmployee(EMPLOYEE_ID);

            assertThat(existingEmployee.getArchivedAt()).isNotNull();
        }

        @Test
        @DisplayName("should unarchive employee by clearing archivedAt")
        void shouldUnarchiveEmployeeByClearingArchivedAt() {
            Employee existingEmployee = employee();
            existingEmployee.setArchivedAt(ARCHIVED_AT);

            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(existingEmployee));

            employeeService.unarchiveEmployee(EMPLOYEE_ID);

            assertThat(existingEmployee.getArchivedAt()).isNull();
        }

        @Test
        @DisplayName("should delete employee, user and reassign events to ghost")
        void shouldDeleteEmployeeUserAndReassignEventsToGhost() {
            Employee existingEmployee = employee();

            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(existingEmployee));
            when(userRepo.findByEmployeeId(EMPLOYEE_ID)).thenReturn(Optional.empty());

            employeeService.deleteEmployee(EMPLOYEE_ID);

            verify(eventRepo).reassignEmployeeEventsToGhost(EMPLOYEE_ID, GHOST_EMPLOYEE_ID);
            verify(userRepo).findByEmployeeId(EMPLOYEE_ID);
            verify(employeeRepo).delete(existingEmployee);
        }

        @Test
        @DisplayName("should delete associated user when employee is deleted")
        void shouldDeleteAssociatedUserWhenEmployeeIsDeleted() {
            Employee existingEmployee = employee();
            com.aprimorar.api.domain.auth.User associatedUser = org.mockito.Mockito.mock(com.aprimorar.api.domain.auth.User.class);

            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(existingEmployee));
            when(userRepo.findByEmployeeId(EMPLOYEE_ID)).thenReturn(Optional.of(associatedUser));

            employeeService.deleteEmployee(EMPLOYEE_ID);

            verify(userRepo).delete(associatedUser);
            verify(employeeRepo).delete(existingEmployee);
        }
    }

    @Nested
    @DisplayName("Query methods")
    class QueryMethods {

        @Test
        @DisplayName("should return paged employees")
        void shouldReturnPagedEmployees() {
            Pageable input = PageRequest.of(0, 2);
            Employee firstEmployee = employee();
            Employee secondEmployee = secondEmployee();
            EmployeeResponseDTO expectedFirst = response();
            EmployeeResponseDTO expectedSecond = secondResponse();
            Page<Employee> expectedPage = new PageImpl<>(List.of(firstEmployee, secondEmployee), input, 2);

            when(employeeRepo.findAll(any(Specification.class), eq(input))).thenReturn(expectedPage);
            when(employeeMapper.convertToDto(firstEmployee)).thenReturn(expectedFirst);
            when(employeeMapper.convertToDto(secondEmployee)).thenReturn(expectedSecond);

            PageDTO<EmployeeResponseDTO> actual = employeeService.getEmployees(input, null, false);

            assertThat(actual.content()).containsExactly(expectedFirst, expectedSecond);
            assertThat(actual.totalElements()).isEqualTo(2);
        }

        @Test
        @DisplayName("should return employee by id")
        void shouldReturnEmployeeById() {
            Employee input = employee();
            EmployeeResponseDTO expected = response();

            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(input));
            when(employeeMapper.convertToDto(input)).thenReturn(expected);

            EmployeeResponseDTO actual = employeeService.findById(EMPLOYEE_ID);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("should throw when employee is not found by id")
        void shouldThrowWhenEmployeeIsNotFoundById() {
            when(employeeRepo.findById(MISSING_EMPLOYEE_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> employeeService.findById(MISSING_EMPLOYEE_ID))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Colaborador não encontrado no Banco de Dados");
        }

        @Test
        @DisplayName("should return summary for employee")
        void shouldReturnSummaryForEmployee() {
            Instant start = Instant.parse("2026-04-01T00:00:00Z");
            Instant end = Instant.parse("2026-04-30T23:59:59.999Z");
            long totalEvents = 10L;
            BigDecimal totalPaid = new BigDecimal("1000.00");
            BigDecimal totalUnpaid = new BigDecimal("500.00");

            when(employeeRepo.existsById(EMPLOYEE_ID)).thenReturn(true);
            when(eventRepo.countByEmployeeIdAndStartDateBetween(eq(EMPLOYEE_ID), any(Instant.class), any(Instant.class)))
                .thenReturn(totalEvents);
            when(eventRepo.sumPaidByEmployeeIdInPeriod(eq(EMPLOYEE_ID), any(Instant.class), any(Instant.class)))
                .thenReturn(totalPaid);
            when(eventRepo.sumUnpaidByEmployeeIdInPeriod(eq(EMPLOYEE_ID), any(Instant.class), any(Instant.class)))
                .thenReturn(totalUnpaid);

            EmployeeSummaryDTO actual = employeeService.getSummary(EMPLOYEE_ID, start, end);

            assertThat(actual.totalEvents()).isEqualTo(totalEvents);
            assertThat(actual.totalPaid()).isEqualTo(totalPaid);
            assertThat(actual.totalUnpaid()).isEqualTo(totalUnpaid);
            verify(employeeRepo).existsById(EMPLOYEE_ID);
        }

        @Test
        @DisplayName("should return all-time summary when start and end dates are null")
        void shouldReturnAllTimeSummaryWhenStartAndEndDatesAreNull() {
            BigDecimal totalPaid = new BigDecimal("500.00");
            BigDecimal totalUnpaid = new BigDecimal("250.00");

            when(employeeRepo.existsById(EMPLOYEE_ID)).thenReturn(true);
            when(eventRepo.countByEmployeeId(EMPLOYEE_ID)).thenReturn(5L);
            when(eventRepo.sumPaidByEmployeeId(EMPLOYEE_ID)).thenReturn(totalPaid);
            when(eventRepo.sumUnpaidByEmployeeId(EMPLOYEE_ID)).thenReturn(totalUnpaid);

            EmployeeSummaryDTO actual = employeeService.getSummary(EMPLOYEE_ID, null, null);

            assertThat(actual.totalEvents()).isEqualTo(5L);
            assertThat(actual.totalPaid()).isEqualTo(totalPaid);
            assertThat(actual.totalUnpaid()).isEqualTo(totalUnpaid);
            verify(eventRepo).countByEmployeeId(EMPLOYEE_ID);
            verify(eventRepo).sumPaidByEmployeeId(EMPLOYEE_ID);
            verify(eventRepo).sumUnpaidByEmployeeId(EMPLOYEE_ID);
        }

        @Test
        @DisplayName("should throw when employee is not found for summary")
        void shouldThrowWhenEmployeeIsNotFoundForSummary() {
            when(employeeRepo.existsById(MISSING_EMPLOYEE_ID)).thenReturn(false);

            assertThatThrownBy(() -> employeeService.getSummary(MISSING_EMPLOYEE_ID, null, null))
                .isInstanceOf(EmployeeNotFoundException.class)
                .hasMessage("Colaborador com o ID informado não encontrado");
        }

        @Test
        @DisplayName("should return employee options")
        void shouldReturnEmployeeOptions() {
            Employee firstEmployee = employee();
            Employee secondEmployee = secondEmployee();

            when(employeeRepo.findAll(any(Specification.class), eq(Sort.by(Sort.Direction.ASC, "name"))))
                .thenReturn(List.of(firstEmployee, secondEmployee));

            List<EmployeeOptionsDTO> actual = employeeService.getEmployeeOptions();

            assertThat(actual)
                .containsExactly(
                    new EmployeeOptionsDTO(EMPLOYEE_ID, "Ana Paula"),
                    new EmployeeOptionsDTO(SECOND_EMPLOYEE_ID, "Carlos Lima")
                );
        }
    }

    private static EmployeeRequestDTO request() {
        return new EmployeeRequestDTO(
            "Ana Paula",
            LocalDate.of(1990, 8, 20),
            "ana@email.com",
            "(61) 98888-8888",
            "109.876.543-21",
            "ana@email.com",
            Duty.TEACHER
        );
    }

    private static EmployeeRequestDTO updatedRequest() {
        return new EmployeeRequestDTO(
            "Ana Beatriz",
            LocalDate.of(1991, 1, 10),
            "ana.pix",
            "(11) 99999-8888",
            "000.000.000-00",
            "ana.beatriz@email.com",
            Duty.ADM
        );
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
        input.setCreatedAt(CREATED_AT);
        input.setUpdatedAt(UPDATED_AT);
        return input;
    }

    private static Employee secondEmployee() {
        Employee input = new Employee(
            "Carlos Lima",
            LocalDate.of(1985, 4, 15),
            "carlos@email.com",
            "61977777777",
            "12345678901",
            "carlos@email.com",
            Duty.MENTOR
        );
        input.setId(SECOND_EMPLOYEE_ID);
        input.setCreatedAt(CREATED_AT);
        input.setUpdatedAt(UPDATED_AT);
        return input;
    }

    private static EmployeeResponseDTO response() {
        return new EmployeeResponseDTO(
            EMPLOYEE_ID,
            "Ana Paula",
            LocalDate.of(1990, 8, 20),
            "ana@email.com",
            "61988888888",
            "10987654321",
            "ana@email.com",
            Duty.TEACHER,
            null,
            CREATED_AT,
            UPDATED_AT
        );
    }

    private static EmployeeResponseDTO updatedResponse() {
        return new EmployeeResponseDTO(
            EMPLOYEE_ID,
            "Ana Beatriz",
            LocalDate.of(1991, 1, 10),
            "ana.pix",
            "11999998888",
            "10987654321",
            "ana.beatriz@email.com",
            Duty.ADM,
            null,
            CREATED_AT,
            UPDATED_AT
        );
    }

    private static EmployeeResponseDTO secondResponse() {
        return new EmployeeResponseDTO(
            SECOND_EMPLOYEE_ID,
            "Carlos Lima",
            LocalDate.of(1985, 4, 15),
            "carlos@email.com",
            "61977777777",
            "12345678901",
            "carlos@email.com",
            Duty.MENTOR,
            null,
            CREATED_AT,
            UPDATED_AT
        );
    }
}
