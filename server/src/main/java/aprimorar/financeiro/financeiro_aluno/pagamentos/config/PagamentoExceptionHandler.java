package aprimorar.financeiro.financeiro_aluno.pagamentos.config;

import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.exception.PagamentoAlunoDadosInvalidosException;
import aprimorar.financeiro.financeiro_aluno.pagamentos.domain.exception.PagamentoAlunoNaoEncontradoException;
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
@RestControllerAdvice(basePackages = "aprimorar.financeiro.financeiro_aluno.pagamentos.web")
public class PagamentoExceptionHandler {

    @ExceptionHandler(PagamentoAlunoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        PagamentoAlunoNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, "Pagamento não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(PagamentoAlunoDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        PagamentoAlunoDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, "Dados inválidos do pagamento", ex.getMessage(), request);
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
