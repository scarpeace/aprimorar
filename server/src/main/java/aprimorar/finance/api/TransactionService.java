package aprimorar.finance.api;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public interface TransactionService {

    void createAppointmentTransactions(UUID appointmentId, BigDecimal price, BigDecimal payment);

    void syncAppointmentTransactions(UUID appointmentId, BigDecimal price, BigDecimal payment);

    void syncStudentCharge(UUID appointmentId, Instant settledAt);

    void syncEmployeePayment(UUID appointmentId, Instant settledAt);

    void deleteAppointmentTransactions(UUID appointmentId);
}
