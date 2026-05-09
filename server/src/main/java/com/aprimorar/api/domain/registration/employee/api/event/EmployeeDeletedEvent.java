package com.aprimorar.api.domain.registration.employee.api.event;

import java.util.UUID;

/**
 * Evento disparado quando um colaborador é removido do sistema.
 */
public record EmployeeDeletedEvent(UUID employeeId) {
}
