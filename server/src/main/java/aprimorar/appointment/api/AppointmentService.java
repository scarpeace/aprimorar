package aprimorar.appointment.api;

import aprimorar.appointment.api.dto.ContentDistributionDTO;
import aprimorar.appointment.api.dto.AppointmentRequestDTO;
import aprimorar.appointment.api.dto.AppointmentResponseDTO;
import aprimorar.shared.PageDTO;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface AppointmentService {

    AppointmentResponseDTO createAppointment(AppointmentRequestDTO dto);

   PageDTO<AppointmentResponseDTO> getAppointments(Pageable pageable);

    AppointmentResponseDTO findById(UUID appointmentId);

    PageDTO<AppointmentResponseDTO> getAppointmentsByEmployeeId(Pageable pageable,UUID employeeId);

   PageDTO<AppointmentResponseDTO> getAppointmentsByStudentId(Pageable pageable,UUID studentId, Instant startDate, Instant endDate);

    AppointmentResponseDTO updateAppointment(UUID id, AppointmentRequestDTO dto);

    void deleteAppointment(UUID appointmentId);

    AppointmentResponseDTO toggleStudentCharge(UUID id);

    AppointmentResponseDTO toggleEmployeePayment(UUID id);

    long countByStudentId(UUID studentId);

//    long countByStudentIdAndStartDateBetween(UUID studentId, Instant startDate, Instant endDate);
//
//    BigDecimal sumChargedByStudentId(UUID studentId);
//
//    BigDecimal sumPendingByStudentId(UUID studentId);
//
//    BigDecimal sumChargedByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate);
//
//    BigDecimal sumPendingByStudentIdInPeriod(UUID studentId, Instant startDate, Instant endDate);

    long countByEmployeeId(UUID employeeId);

//    long countByEmployeeIdAndStartDateBetween(UUID employeeId, Instant startDate, Instant endDate);
//
//    BigDecimal sumPaidByEmployeeId(UUID employeeId);
//
//    BigDecimal sumUnpaidByEmployeeId(UUID employeeId);
//
//    BigDecimal sumPaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate);
//
//    BigDecimal sumUnpaidByEmployeeIdInPeriod(UUID employeeId, Instant startDate, Instant endDate);

    void reassignStudentAppointmentsToGhost(UUID studentId);

    void reassignEmployeeAppointmentsToGhost(UUID employeeId);

    long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId);

    long countAppointmentsInPeriod(Instant startDate, Instant endDate);


    List<ContentDistributionDTO> findContentDistributionInPeriod(Instant startDate, Instant endDate);
}
