package aprimorar.appointment.api.exception;

public class NotAllowedToUpdateAppointmentException extends RuntimeException {
    public NotAllowedToUpdateAppointmentException(String message) {
        super(message);
    }
}
