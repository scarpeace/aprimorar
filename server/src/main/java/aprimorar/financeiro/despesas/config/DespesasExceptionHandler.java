package aprimorar.financeiro.despesas.config;

import aprimorar.financeiro.despesas.domain.exception.DespesaDadosInvalidosException;
import aprimorar.financeiro.despesas.domain.exception.DespesaNaoEncontradaException;
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
@RestControllerAdvice(basePackages = "aprimorar.financeiro.despesas.web")
public class DespesasExceptionHandler {

    @ExceptionHandler(DespesaDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        DespesaDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        body.setTitle("Dados inválidos da despesa");
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.badRequest().body(body);
    }

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
