package aprimorar.event.api.exception;

public class NotAllowedToUpdateEventException extends RuntimeException {
    public NotAllowedToUpdateEventException(String message) {
        super(message);
    }
}
