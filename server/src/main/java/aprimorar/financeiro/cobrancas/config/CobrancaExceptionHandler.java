package aprimorar.financeiro.cobrancas.config;

import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.financeiro.cobrancas.web")
public class CobrancaExceptionHandler {

    @ExceptionHandler(CobrancaIndividualNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        CobrancaIndividualNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, "Cobrança não encontrada", ex.getMessage(), request);
    }

    @ExceptionHandler(CobrancaIndividualDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        CobrancaIndividualDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, "Dados inválidos da cobrança", ex.getMessage(), request);
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
