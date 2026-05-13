package aprimorar.appointment.api.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento disparado quando o pagamento do colaborador em um appointment e alternado.
 */
public record EmployeePaymentToggledEvent(UUID appointmentId, Instant settledAt) {
}
