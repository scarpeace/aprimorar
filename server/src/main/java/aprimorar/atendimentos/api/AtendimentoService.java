package aprimorar.atendimentos.api;

import aprimorar.atendimentos.api.dto.ContentDistributionDTO;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface AtendimentoService {

    void reassignStudentAppointmentsToGhost(UUID studentId);

    void reassignEmployeeAppointmentsToGhost(UUID employeeId);

    long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId);

    long countAppointmentsInPeriod(Instant startDate, Instant endDate);

    List<ContentDistributionDTO> findContentDistributionInPeriod(Instant startDate, Instant endDate);
}
