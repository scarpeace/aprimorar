package aprimorar.agendamento.colaboradores.config;


import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorPossuiRepassePendenteException;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorDuplicadoException;
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
@RestControllerAdvice(basePackages = "aprimorar.agendamento.colaboradores.web")
public class ColaboradorExceptionHandler {

    @ExceptionHandler(ColaboradorNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ColaboradorNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Colaborador não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(ColaboradorDuplicadoException.class)
    public ResponseEntity<ProblemDetail> handleConflict(ColaboradorDuplicadoException ex, HttpServletRequest request) {
        return response(HttpStatus.CONFLICT, "Colaborador duplicado", ex.getMessage(), request);
    }

    @ExceptionHandler(ColaboradorPossuiRepassePendenteException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        ColaboradorPossuiRepassePendenteException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, "Pendência financeira do colaborador", ex.getMessage(), request);
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
