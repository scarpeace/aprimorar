package aprimorar.appointment.api.exception;

public class AppointmentScheduleConflictException extends RuntimeException {
    public AppointmentScheduleConflictException(String message) {
        super(message);
    }
}
