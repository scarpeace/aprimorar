package aprimorar.auth.config;

import aprimorar.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "aprimorar.auth")
public class AuthExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ProblemDetail handleUnauthorized(
        AuthException ex,
        HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage()
        );

        problem.setTitle("Não autorizado");
        problem.setInstance(URI.create(request.getRequestURI()));

        return problem;
    }
}
