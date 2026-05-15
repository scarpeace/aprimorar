package aprimorar.appointment.api;

import aprimorar.appointment.api.dto.ContentDistributionDTO;
import aprimorar.appointment.api.dto.AppointmentFinanceSummaryDTO;
import aprimorar.appointment.api.dto.AppointmentRequestDTO;
import aprimorar.appointment.api.dto.AppointmentResponseDTO;
import aprimorar.appointment.api.dto.EmployeeAppointmentsResponseDTO;
import aprimorar.appointment.api.dto.EmployeesFinanceSummaryResponseDTO;
import aprimorar.appointment.api.dto.StudentsFinanceSummaryResponseDTO;
import aprimorar.appointment.api.dto.StudentAppointmentsResponseDTO;
import aprimorar.shared.PageDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

    PageDTO<AppointmentResponseDTO> getAppointments(
        Pageable pageable,
        String search,
        Instant startDate,
        Instant endDate,
        Boolean hideCharged,
        Boolean hidePaid
    );

    AppointmentResponseDTO findById(UUID appointmentId);

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

    AppointmentResponseDTO updateAppointment(UUID id, AppointmentRequestDTO dto);

    void deleteAppointment(UUID appointmentId);

    AppointmentResponseDTO toggleStudentCharge(UUID id);

    AppointmentResponseDTO toggleEmployeePayment(UUID id);

    void reassignStudentAppointmentsToGhost(UUID studentId);

    void reassignEmployeeAppointmentsToGhost(UUID employeeId);

    long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId);

    long countAppointmentsInPeriod(Instant startDate, Instant endDate);

    List<ContentDistributionDTO> findContentDistributionInPeriod(Instant startDate, Instant endDate);
}
