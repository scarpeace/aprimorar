package aprimorar.appointment.api.event;

import java.time.Instant;
import java.util.UUID;

/**
 * Evento disparado quando a cobranca do aluno em um appointment e alternada.
 */
public record StudentChargeToggledEvent(UUID appointmentId, Instant settledAt) {
}
