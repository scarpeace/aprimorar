package aprimorar.financeiro.pagamentos_particular.config;

import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularDadosInvalidosException;
import aprimorar.financeiro.pagamentos_particular.domain.exception.PagamentoParticularNaoEncontradoException;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularDadosInvalidosException;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.financeiro.pagamentos_particular.web")
public class PagamentoParticularExceptionHandler {

    @ExceptionHandler(RepasseParticularNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        RepasseParticularNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, "Repasse não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(RepasseParticularDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        RepasseParticularDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, "Dados inválidos do repasse", ex.getMessage(), request);
    }

    @ExceptionHandler(PagamentoParticularNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handlePagamentoNotFound(
        PagamentoParticularNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, "Pagamento de repasse não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(PagamentoParticularDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handlePagamentoBadRequest(
        PagamentoParticularDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(
            HttpStatus.BAD_REQUEST,
            "Dados inválidos do pagamento de repasse",
            ex.getMessage(),
            request
        );
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
