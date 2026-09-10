package aprimorar.atendimentos.individuais.config;

import aprimorar.atendimentos.individuais.domain.exception.AtendimentoConflitanteException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoDadosInvalidosException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoEdicaoExpiradaException;
import aprimorar.atendimentos.individuais.domain.exception.AtendimentoNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.atendimentos.individuais.web.controller")
public class AtendimentosExceptionHandler {

    @ExceptionHandler(AtendimentoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(AtendimentoNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Atendimento não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler({
        AtendimentoConflitanteException.class,
        AtendimentoDadosInvalidosException.class,
        AtendimentoEdicaoExpiradaException.class
    })
    public ResponseEntity<ProblemDetail> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
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
