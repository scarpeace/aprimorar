package aprimorar.despesas.config;

import aprimorar.despesas.domain.exception.DespesaNaoEncontradaException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DespesasExceptionHandler {

    @ExceptionHandler(DespesaNaoEncontradaException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(DespesaNaoEncontradaException ex, HttpServletRequest request) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage() == null ? HttpStatus.NOT_FOUND.getReasonPhrase() : ex.getMessage()
        );
        body.setTitle("Despesa não encontrada");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
