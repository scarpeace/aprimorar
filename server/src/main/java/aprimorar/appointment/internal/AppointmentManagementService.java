package aprimorar.appointment.internal;

import aprimorar.appointment.api.dto.AppointmentFinanceSummaryDTO;
import aprimorar.appointment.api.dto.AppointmentRequestDTO;
import aprimorar.appointment.api.dto.AppointmentResponseDTO;
import aprimorar.appointment.api.dto.EmployeeAppointmentsResponseDTO;
import aprimorar.appointment.api.dto.EmployeesFinanceSummaryResponseDTO;
import aprimorar.appointment.api.dto.EmployeesWithFinanceResponseDTO;
import aprimorar.appointment.api.dto.StudentAppointmentsResponseDTO;
import aprimorar.appointment.api.dto.StudentsFinanceSummaryResponseDTO;
import aprimorar.appointment.api.dto.StudentsWithFinanceResponseDTO;
import aprimorar.shared.PageDTO;
import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

interface AppointmentManagementService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

    PageDTO<AppointmentResponseDTO> getAppointments(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        Boolean hideCharged,
        Boolean hidePaid
    );

    AppointmentResponseDTO findById(UUID id);

    EmployeeAppointmentsResponseDTO getAppointmentsByEmployeeId(
        Pageable pageable,
        UUID employeeId,
        Boolean hidePaid,
        Instant startDate,
        Instant endDate
    );

    StudentAppointmentsResponseDTO getAppointmentsByStudentId(
        Pageable pageable,
        UUID studentId,
        Instant startDate,
        Instant endDate,
        Boolean charged
    );

    AppointmentFinanceSummaryDTO getFinanceReport(Instant startDate, Instant endDate);

    StudentsFinanceSummaryResponseDTO getStudentsFinanceReport(Instant startDate, Instant endDate);

    EmployeesFinanceSummaryResponseDTO getEmployeesFinanceReport(Instant startDate, Instant endDate);

    EmployeesWithFinanceResponseDTO getEmployeesWithFinance(
        Pageable pageable,
        String search,
        Boolean archived,
        Instant startDate,
        Instant endDate
    );

    StudentsWithFinanceResponseDTO getStudentsWithFinance(
        Pageable pageable,
        String search,
        Boolean archived,
        Instant startDate,
        Instant endDate
    );

    AppointmentResponseDTO updateAppointment(UUID id, AppointmentRequestDTO dto);

    void deleteAppointment(UUID id);

    AppointmentResponseDTO toggleStudentCharge(UUID id);

    AppointmentResponseDTO toggleEmployeePayment(UUID id);
}
