package aprimorar.shared.exception;

import org.springframework.http.HttpStatus;

public class DomainBusinessException extends RuntimeException {

    private final HttpStatus status;

    public DomainBusinessException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
