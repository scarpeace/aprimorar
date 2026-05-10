package aprimorar.event.internal.listener;

import aprimorar.event.api.EventService;
import aprimorar.registration.employee.api.event.EmployeeDeletedEvent;
import aprimorar.registration.student.api.event.StudentDeletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RegistrationEventListener {

    private final EventService eventService;

    public RegistrationEventListener(EventService eventService) {
        this.eventService = eventService;
    }

    @EventListener
    public void onStudentDeleted(StudentDeletedEvent event) {
        eventService.reassignStudentEventsToGhost(event.studentId());
    }

    @EventListener
    public void onEmployeeDeleted(EmployeeDeletedEvent event) {
        eventService.reassignEmployeeEventsToGhost(event.employeeId());
    }
}
