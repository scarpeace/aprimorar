package aprimorar.atendimentos.api;

import java.time.Instant;
import java.util.UUID;

public interface AtendimentosQueryApi {

    boolean alunoHasPendingCharges(UUID alunoId);
    boolean colaboradorHasPendingPayment(UUID colaboradorId);
    long countActiveStudentsInPeriod(Instant startDate, Instant endDate, UUID excludedStudentId);
    long countAppointmentsInPeriod(Instant startDate, Instant endDate);
}
