package com.aprimorar.api.domain.employee;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.employee.dto.EmployeeMonthlySummaryDTO;
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
        @DisplayName("should delete employee and reassign events to ghost")
        void shouldDeleteEmployeeAndReassignEventsToGhost() {
            Employee existingEmployee = employee();

            when(employeeRepo.findById(EMPLOYEE_ID)).thenReturn(Optional.of(existingEmployee));

            employeeService.deleteEmployee(EMPLOYEE_ID);

            verify(eventRepo).reassignEmployeeEventsToGhost(EMPLOYEE_ID, GHOST_EMPLOYEE_ID);
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
        @DisplayName("should return monthly summary for employee")
        void shouldReturnMonthlySummaryForEmployee() {
            int month = 4;
            int year = 2026;
            ZoneId zoneId = ZoneId.of("UTC");
            Instant fixedInstant = Instant.parse("2026-04-15T10:00:00Z");
            long totalEvents = 10L;
            BigDecimal totalPayment = new BigDecimal("1500.00");

            when(employeeRepo.existsById(EMPLOYEE_ID)).thenReturn(true);
            when(clock.instant()).thenReturn(fixedInstant);
            when(clock.getZone()).thenReturn(zoneId);
            when(eventRepo.countByEmployeeIdAndStartDateBetween(eq(EMPLOYEE_ID), any(Instant.class), any(Instant.class)))
                .thenReturn(totalEvents);
            when(eventRepo.sumPaymentByEmployeeIdInPeriod(eq(EMPLOYEE_ID), any(Instant.class), any(Instant.class)))
                .thenReturn(totalPayment);

            EmployeeMonthlySummaryDTO actual = employeeService.getMonthlySummary(EMPLOYEE_ID, month, year);

            assertThat(actual.totalEvents()).isEqualTo(totalEvents);
            assertThat(actual.totalPayment()).isEqualTo(totalPayment);
            verify(employeeRepo).existsById(EMPLOYEE_ID);
        }

        @Test
        @DisplayName("should return monthly summary using current date when month and year are null")
        void shouldReturnMonthlySummaryUsingCurrentDateWhenMonthAndYearAreNull() {
            ZoneId zoneId = ZoneId.of("UTC");
            Instant fixedInstant = Instant.parse("2026-04-15T10:00:00Z");

            when(employeeRepo.existsById(EMPLOYEE_ID)).thenReturn(true);
            when(clock.instant()).thenReturn(fixedInstant);
            when(clock.getZone()).thenReturn(zoneId);
            when(eventRepo.countByEmployeeIdAndStartDateBetween(eq(EMPLOYEE_ID), any(Instant.class), any(Instant.class)))
                .thenReturn(5L);
            when(eventRepo.sumPaymentByEmployeeIdInPeriod(eq(EMPLOYEE_ID), any(Instant.class), any(Instant.class)))
                .thenReturn(new BigDecimal("750.00"));

            EmployeeMonthlySummaryDTO actual = employeeService.getMonthlySummary(EMPLOYEE_ID, null, null);

            assertThat(actual.totalEvents()).isEqualTo(5L);
            assertThat(actual.totalPayment()).isEqualTo(new BigDecimal("750.00"));
        }

        @Test
        @DisplayName("should throw when employee is not found for monthly summary")
        void shouldThrowWhenEmployeeIsNotFoundForMonthlySummary() {
            when(employeeRepo.existsById(MISSING_EMPLOYEE_ID)).thenReturn(false);

            assertThatThrownBy(() -> employeeService.getMonthlySummary(MISSING_EMPLOYEE_ID, 4, 2026))
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
