package aprimorar.auth.config;

import aprimorar.auth.exception.UserAlreadyExistsException;
import aprimorar.auth.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "aprimorar.auth.web")
public class UserExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleConflict(UserAlreadyExistsException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Usuário já existe", ex.getMessage(), request);
    }

    private ResponseEntity<ProblemDetail> response(
        HttpStatus status,
        String title,
        String detail,
        HttpServletRequest request
    ) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
            status,
            detail == null ? status.getReasonPhrase() : detail
        );
        body.setTitle(title);
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(status).body(body);
    }
}
