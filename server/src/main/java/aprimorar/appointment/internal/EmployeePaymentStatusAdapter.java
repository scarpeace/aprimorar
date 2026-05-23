package aprimorar.appointment.internal;

import aprimorar.appointment.internal.repository.AppointmentRepository;
import aprimorar.registration.employee.api.contract.EmployeePaymentStatusPort;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmployeePaymentStatusAdapter implements EmployeePaymentStatusPort {

    private final AppointmentRepository appointmentRepository;

    public EmployeePaymentStatusAdapter(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPendingEmployeePayments(UUID employeeId) {
        return appointmentRepository.existsByEmployeeIdAndEmployeePaymentDateIsNull(employeeId);
    }
}
