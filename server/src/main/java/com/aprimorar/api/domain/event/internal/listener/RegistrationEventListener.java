package com.aprimorar.api.domain.event.internal.listener;

import com.aprimorar.api.domain.event.api.EventService;
import com.aprimorar.api.domain.registration.employee.api.event.EmployeeDeletedEvent;
import com.aprimorar.api.domain.registration.student.api.event.StudentDeletedEvent;
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
