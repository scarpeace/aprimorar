package aprimorar.appointment.api;

import aprimorar.appointment.api.dto.ContentDistributionDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    void reassignStudentAppointmentsToGhost(UUID studentId);

    void reassignEmployeeAppointmentsToGhost(UUID employeeId);

    long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId);

    boolean hasPendingEmployeePayments(UUID employeeId);

    long countAppointmentsInPeriod(Instant startDate, Instant endDate);

    List<ContentDistributionDTO> findContentDistributionInPeriod(Instant startDate, Instant endDate);
}
