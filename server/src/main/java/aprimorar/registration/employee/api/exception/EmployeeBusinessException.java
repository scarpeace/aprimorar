package aprimorar.registration.employee.api.exception;

import org.springframework.http.HttpStatus;

public class EmployeeBusinessException extends RuntimeException {

    private final HttpStatus status;

    public EmployeeBusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
