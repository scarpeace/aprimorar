package aprimorar.auth.config;

import aprimorar.auth.usuario.domain.exception.UsuarioDadosInvalidosException;
import aprimorar.auth.usuario.domain.exception.UsuarioDuplicadoException;
import aprimorar.auth.usuario.domain.exception.UsuarioEstadoInvalidoException;
import aprimorar.auth.usuario.domain.exception.UsuarioNaoEncontradoException;
import aprimorar.common.models.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ErrorResponse> handleNotFound(UsuarioNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Usuário não encontrado", ex.getMessage());
    }

    @ExceptionHandler({ UsuarioDuplicadoException.class, UsuarioEstadoInvalidoException.class })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Erro de regra de negócio", ex.getMessage());
    }

    @ExceptionHandler(UsuarioDadosInvalidosException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(UsuarioDadosInvalidosException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Erro de regra de negócio", ex.getMessage());
    }

    @ExceptionHandler({ AuthenticationCredentialsNotFoundException.class, BadCredentialsException.class, DisabledException.class })
    @ApiResponse(
        responseCode = "401",
        description = "Não autenticado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<ErrorResponse> handleUnauthorizedExceptions(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de autenticação: {}", ex.getMessage());
        return response(HttpStatus.UNAUTHORIZED, "Erro de autenticação", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String error, String message) {
        ErrorResponse body = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            error,
            List.of(message)
        );
        return ResponseEntity.status(status).body(body);
    }
}

