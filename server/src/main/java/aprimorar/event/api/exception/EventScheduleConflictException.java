package aprimorar.event.api.exception;

public class EventScheduleConflictException extends RuntimeException {
    public EventScheduleConflictException(String message) {
        super(message);
    }
}
