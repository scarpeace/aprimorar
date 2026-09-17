package aprimorar.atendimentos.individuais.config;

import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualEdicaoExpiradaException;
import aprimorar.atendimentos.individuais.atendimentos.domain.exception.AtendimentoIndividualNaoEncontradoException;

import aprimorar.financeiro.repasses.domain.exception.RepasseIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualDadosInvalidosException;
import aprimorar.financeiro.cobrancas.domain.exception.CobrancaIndividualNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.atendimentos.individuais")
public class AtendimentosExceptionHandler {

    @ExceptionHandler(AtendimentoIndividualNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(AtendimentoIndividualNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Atendimento não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler({ CobrancaIndividualNaoEncontradoException.class, RepasseIndividualNaoEncontradoException.class })
    public ResponseEntity<ProblemDetail> handleFinancialNotFound(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Registro financeiro não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler({
        AtendimentoIndividualConflitanteException.class,
        AtendimentoIndividualDadosInvalidosException.class,
        AtendimentoIndividualEdicaoExpiradaException.class,
        CobrancaIndividualDadosInvalidosException.class,
        RepasseIndividualDadosInvalidosException.class,
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
