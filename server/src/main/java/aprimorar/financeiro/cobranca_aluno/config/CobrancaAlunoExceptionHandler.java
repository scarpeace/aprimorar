package aprimorar.financeiro.cobranca_aluno.config;

import aprimorar.financeiro.cobranca_aluno.domain.exception.CobrancaAlunoDadosInvalidosException;
import aprimorar.financeiro.cobranca_aluno.domain.exception.CobrancaAlunoNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.financeiro.cobranca_aluno.web.controller")
public class CobrancaAlunoExceptionHandler {

    @ExceptionHandler(CobrancaAlunoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        CobrancaAlunoNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, "Cobrança do aluno não encontrada", ex.getMessage(), request);
    }

    @ExceptionHandler({ CobrancaAlunoDadosInvalidosException.class, IllegalArgumentException.class })
    public ResponseEntity<ProblemDetail> handleBadRequest(
        RuntimeException ex,
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
        ProblemDetail body = ProblemDetail.forStatusAndDetail(status, detail);
        body.setTitle(title);
        body.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(status).body(body);
    }
}
