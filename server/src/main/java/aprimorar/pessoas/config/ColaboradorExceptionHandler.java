package aprimorar.pessoas.config;

import aprimorar.pessoas.domain.exception.ColaboradorDuplicadoException;
import aprimorar.pessoas.domain.exception.ColaboradorEstadoInvalidoException;
import aprimorar.pessoas.domain.exception.ColaboradorNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.pessoas.web.controller")
public class ColaboradorExceptionHandler {

    @ExceptionHandler(ColaboradorNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ColaboradorNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Colaborador não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(ColaboradorDuplicadoException.class)
    public ResponseEntity<ProblemDetail> handleConflict(ColaboradorDuplicadoException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Colaborador duplicado", ex.getMessage(), request);
    }

    @ExceptionHandler(ColaboradorEstadoInvalidoException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(ColaboradorEstadoInvalidoException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Estado inválido do colaborador", ex.getMessage(), request);
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
