package aprimorar.appointment.internal.listener;

import aprimorar.appointment.api.AppointmentService;
import aprimorar.registration.employee.api.event.EmployeeDeletedEvent;
import aprimorar.registration.student.api.event.StudentDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppointmentEventReactor {

    private final AppointmentService appointmentService;

    public AppointmentEventReactor(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @EventListener
    public void onStudentDeleted(StudentDeletedEvent event) {
        appointmentService.reassignStudentAppointmentsToGhost(event.studentId());
    }

    @EventListener
    public void onEmployeeDeleted(EmployeeDeletedEvent event) {
        appointmentService.reassignEmployeeAppointmentsToGhost(event.employeeId());
    }
}
