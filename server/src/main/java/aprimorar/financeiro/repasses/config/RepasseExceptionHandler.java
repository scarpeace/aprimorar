package aprimorar.financeiro.repasses.config;

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
@RestControllerAdvice(basePackages = "aprimorar.financeiro.repasses.web")
public class RepasseExceptionHandler {

    @ExceptionHandler(RepasseIndividualNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        RepasseIndividualNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, "Repasse não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(RepasseIndividualDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        RepasseIndividualDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, "Dados inválidos do repasse", ex.getMessage(), request);
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
