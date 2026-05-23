package aprimorar.registration.employee.api.contract;

import java.util.UUID;

public interface EmployeePaymentStatusPort {

    boolean hasPendingEmployeePayments(UUID employeeId);
}
