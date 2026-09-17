package aprimorar.financeiro.config;

import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualNaoEncontradoException;
import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualDadosInvalidosException;
import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.financeiro")
public class FinanceiroExceptionHandler {

    @ExceptionHandler({
        CobrancaIndividualNaoEncontradoException.class,
        RepasseIndividualNaoEncontradoException.class
    })
    public ResponseEntity<ProblemDetail> handleNotFound(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, "Registro financeiro não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler({
        CobrancaIndividualDadosInvalidosException.class,
        RepasseIndividualDadosInvalidosException.class
    })
    public ResponseEntity<ProblemDetail> handleBadRequest(
        RuntimeException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, "Erro de regra de negócio", ex.getMessage(), request);
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
