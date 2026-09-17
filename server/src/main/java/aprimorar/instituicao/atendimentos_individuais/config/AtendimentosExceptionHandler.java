package aprimorar.instituicao.atendimentos_individuais.config;

import aprimorar.instituicao.atendimentos_individuais.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.instituicao.atendimentos_individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.instituicao.atendimentos_individuais.domain.exception.AtendimentoIndividualEdicaoExpiradaException;
import aprimorar.instituicao.atendimentos_individuais.domain.exception.AtendimentoIndividualNaoEncontradoException;


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
@RestControllerAdvice(basePackages = "aprimorar.instituicao.atendimentos_individuais")
public class AtendimentosExceptionHandler {

    @ExceptionHandler(AtendimentoIndividualNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(AtendimentoIndividualNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Atendimento não encontrado", ex.getMessage(), request);
    }


    @ExceptionHandler({
        AtendimentoIndividualConflitanteException.class,
        AtendimentoIndividualDadosInvalidosException.class,
        AtendimentoIndividualEdicaoExpiradaException.class,
        IllegalStateException.class
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
