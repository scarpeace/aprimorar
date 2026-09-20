package aprimorar.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static String findConstraintName(Throwable exception) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException violation) {
                return violation.getConstraintName();
            }

            cause = cause.getCause();
        }

        return null;
    }

    public static ResponseEntity<ProblemDetail> response(
        HttpStatus status,
        String errorCode,
        String detail,
        HttpServletRequest request
    ) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
            status,
            detail == null ? status.getReasonPhrase() : detail
        );

        body.setTitle(errorCode);
        body.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(status).body(body);
    }
}
