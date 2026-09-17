package aprimorar.instituicao.alunos.config;

import aprimorar.instituicao.alunos.domain.exception.AlunoNaoEncontradoException;
import aprimorar.instituicao.alunos.domain.exception.AlunoDuplicadoException;
import aprimorar.instituicao.alunos.domain.exception.AlunoEstadoInvalidoException;
import aprimorar.instituicao.alunos.domain.exception.AlunoPossuiPendenciaFinanceiraException;
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
@RestControllerAdvice(basePackages = "aprimorar.instituicao.alunos.web.controller")
public class AlunoExceptionHandler {

    @ExceptionHandler(AlunoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(AlunoNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Aluno não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(AlunoDuplicadoException.class)
    public ResponseEntity<ProblemDetail> handleConflict(AlunoDuplicadoException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Aluno duplicado", ex.getMessage(), request);
    }

    @ExceptionHandler({ AlunoEstadoInvalidoException.class, AlunoPossuiPendenciaFinanceiraException.class })
    public ResponseEntity<ProblemDetail> handleBadRequest(AlunoEstadoInvalidoException ex, HttpServletRequest request) {
        return response(HttpStatus.BAD_REQUEST, "Estado inválido do aluno", ex.getMessage(), request);
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
