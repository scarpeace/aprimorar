package aprimorar.registration.employee.api.event;

import java.util.UUID;

public record EmployeeDeletedEvent(UUID employeeId) {
}
