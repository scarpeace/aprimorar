package com.aprimorar.api.domain.registration.student.api.event;

import java.util.UUID;

/**
 * Evento disparado quando um estudante é removido do sistema.
 */
public record StudentDeletedEvent(UUID studentId) {
}
