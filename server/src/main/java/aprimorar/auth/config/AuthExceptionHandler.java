package aprimorar.auth.config;

import aprimorar.auth.domain.exception.AuthException;
import aprimorar.auth.domain.exception.UserAlreadyExistsException;
import aprimorar.auth.domain.exception.UserNotFoundException;
import aprimorar.common.utils.ExceptionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "aprimorar.auth")
class AuthExceptionHandler {

    private static final String AUTH_NAO_AUTORIZADO = "AUTH_NAO_AUTORIZADO";
    private static final String USUARIO_NAO_ENCONTRADO = "USUARIO_NAO_ENCONTRADO";
    private static final String USUARIO_JA_EXISTENTE = "USUARIO_JA_EXISTENTE";

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorized(
        AuthException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.UNAUTHORIZED,
            AUTH_NAO_AUTORIZADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleUserNotFound(
        UserNotFoundException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            USUARIO_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleUserAlreadyExists(
        UserAlreadyExistsException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.CONFLICT,
            USUARIO_JA_EXISTENTE,
            ex.getMessage(),
            request
        );
    }
}
