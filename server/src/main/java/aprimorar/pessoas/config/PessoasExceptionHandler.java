package aprimorar.pessoas.config;

import aprimorar.pessoas.aluno.domain.exception.AlunoDuplicadoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoEstadoInvalidoException;
import aprimorar.pessoas.aluno.domain.exception.AlunoNaoEncontradoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorDuplicadoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorEstadoInvalidoException;
import aprimorar.pessoas.colaborador.domain.exception.ColaboradorNaoEncontradoException;
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
@RestControllerAdvice
public class PessoasExceptionHandler {

    @ExceptionHandler({
        AlunoNaoEncontradoException.class,
        ColaboradorNaoEncontradoException.class
    })
    public ResponseEntity<ProblemDetail> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Pessoa não encontrada", ex.getMessage(), request);
    }

    @ExceptionHandler({
        AlunoDuplicadoException.class,
        ColaboradorDuplicadoException.class
    })
    public ResponseEntity<ProblemDetail> handleConflict(RuntimeException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Pessoa duplicada", ex.getMessage(), request);
    }

    @ExceptionHandler({
        AlunoEstadoInvalidoException.class,
        ColaboradorEstadoInvalidoException.class
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
