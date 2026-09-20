package aprimorar.agendamento.alunos.config;

import aprimorar.agendamento.alunos.domain.exception.AlunoNaoEncontradoException;
import aprimorar.agendamento.alunos.domain.exception.AlunoPossuiPendenciaFinanceiraException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "aprimorar.agendamento.alunos.web")
public class AlunoExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AlunoExceptionHandler.class);
    private static final String ALUNO_CPF_DUPLICADO = "ALUNO_CPF_DUPLICADO";
    private static final String ALUNO_EMAIL_DUPLICADO = "ALUNO_EMAIL_DUPLICADO";

    @ExceptionHandler(AlunoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(AlunoNaoEncontradoException ex, HttpServletRequest request) {
        return response(HttpStatus.NOT_FOUND, "Aluno não encontrado", ex.getMessage(), request);
    }

    @ExceptionHandler(AlunoPossuiPendenciaFinanceiraException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        AlunoPossuiPendenciaFinanceiraException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, "Pendência financeira do aluno", ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        String constraint = constraintName(ex);

        return switch (constraint == null ? "" : constraint) {
            case "uk_alunos_cpf" -> response(
                HttpStatus.CONFLICT,
                "CPF já cadastrado",
                "Já existe um aluno com o CPF informado.",
                ALUNO_CPF_DUPLICADO,
                request
            );
            case "uk_alunos_email" -> response(
                HttpStatus.CONFLICT,
                "E-mail já cadastrado",
                "Já existe um aluno com o e-mail informado.",
                ALUNO_EMAIL_DUPLICADO,
                request
            );
            default -> {
                log.error("Erro de integridade dos dados do aluno", ex);
                yield response(
                    HttpStatus.CONFLICT,
                    "Conflito de dados",
                    "Os dados informados violam uma restrição existente.",
                    request
                );
            }
        };
    }

    private ResponseEntity<ProblemDetail> response(
        HttpStatus status,
        String title,
        String detail,
        HttpServletRequest request
    ) {
        return response(status, title, detail, null, request);
    }

    private ResponseEntity<ProblemDetail> response(
        HttpStatus status,
        String title,
        String detail,
        String code,
        HttpServletRequest request
    ) {
        ProblemDetail body = ProblemDetail.forStatusAndDetail(
            status,
            detail == null ? status.getReasonPhrase() : detail
        );
        body.setTitle(title);
        body.setInstance(URI.create(request.getRequestURI()));
        if (code != null) {
            body.setProperty("code", code);
        }
        return ResponseEntity.status(status).body(body);
    }

    private static String constraintName(Throwable exception) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof ConstraintViolationException violation) {
                return violation.getConstraintName();
            }

            cause = cause.getCause();
        }

        return null;
    }
}
