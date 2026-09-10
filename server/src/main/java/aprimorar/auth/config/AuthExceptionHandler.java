package aprimorar.auth.config;

import aprimorar.auth.usuario.domain.exception.UsuarioDadosInvalidosException;
import aprimorar.auth.usuario.domain.exception.UsuarioDuplicadoException;
import aprimorar.auth.usuario.domain.exception.UsuarioEstadoInvalidoException;
import aprimorar.auth.usuario.domain.exception.UsuarioNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "aprimorar.auth")
public class AuthExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(UsuarioNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler({ UsuarioDuplicadoException.class, UsuarioEstadoInvalidoException.class })
    public ResponseEntity<ProblemDetail> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Erro de regra de negócio", ex.getMessage(), request);
    }

    @ExceptionHandler(UsuarioDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(UsuarioDadosInvalidosException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Erro de regra de negócio", ex.getMessage(), request);
    }

    @ExceptionHandler({ AuthenticationCredentialsNotFoundException.class, BadCredentialsException.class, DisabledException.class })
    public ResponseEntity<ProblemDetail> handleUnauthorizedExceptions(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de autenticação: {}", ex.getMessage());
        return response(HttpStatus.UNAUTHORIZED, "Erro de autenticação", ex.getMessage(), request);
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
