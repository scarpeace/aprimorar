package aprimorar.registration.api.exception;

public class PersonHasPendingFinancialsException extends RuntimeException {
    public PersonHasPendingFinancialsException(String message) {
        super(message);
    }
}
