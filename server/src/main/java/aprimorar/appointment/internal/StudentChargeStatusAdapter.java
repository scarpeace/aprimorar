package aprimorar.appointment.internal;

import aprimorar.appointment.internal.repository.AppointmentRepository;
import aprimorar.registration.student.api.StudentChargeStatusPort;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class StudentChargeStatusAdapter implements StudentChargeStatusPort {

    private final AppointmentRepository appointmentRepository;

    public StudentChargeStatusAdapter(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPendingStudentCharges(UUID studentId) {
        return appointmentRepository.existsByStudentIdAndStudentChargeDateIsNull(studentId);
    }
}
