package aprimorar.agendamento.alunos.config;

import aprimorar.agendamento.alunos.domain.exception.AlunoNaoEncontradoException;
import aprimorar.agendamento.alunos.domain.exception.AlunoPossuiPendenciaFinanceiraException;
import aprimorar.common.utils.ExceptionUtils;
import jakarta.servlet.http.HttpServletRequest;
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
    private static final String ALUNO_NAO_ENCONTRADO = "ALUNO_NAO_ENCONTRADO";
    private static final String ALUNO_POSSUI_PENDENCIA_FINANCEIRA =
        "ALUNO_POSSUI_PENDENCIA_FINANCEIRA";
    private static final String ALUNO_CPF_DUPLICADO = "ALUNO_CPF_DUPLICADO";
    private static final String ALUNO_EMAIL_DUPLICADO = "ALUNO_EMAIL_DUPLICADO";
    private static final String ALUNO_CONFLITO_DE_DADOS = "ALUNO_CONFLITO_DE_DADOS";

    @ExceptionHandler(AlunoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(AlunoNaoEncontradoException ex, HttpServletRequest request) {
        return ExceptionUtils.response(HttpStatus.NOT_FOUND, ALUNO_NAO_ENCONTRADO, ex.getMessage(), request);
    }

    @ExceptionHandler(AlunoPossuiPendenciaFinanceiraException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        AlunoPossuiPendenciaFinanceiraException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            ALUNO_POSSUI_PENDENCIA_FINANCEIRA,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        String constraint = ExceptionUtils.findConstraintName(ex);

        return switch (constraint == null ? "" : constraint) {
            case "uk_alunos_cpf" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                ALUNO_CPF_DUPLICADO,
                "Já existe um aluno com o CPF informado.",
                request
            );
            case "uk_alunos_email" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                ALUNO_EMAIL_DUPLICADO,
                "Já existe um aluno com o e-mail informado.",
                request
            );
            default -> {
                log.error("Erro de integridade dos dados do aluno", ex);
                yield ExceptionUtils.response(
                    HttpStatus.CONFLICT,
                    ALUNO_CONFLITO_DE_DADOS,
                    "Os dados informados violam uma restrição existente.",
                    request
                );
            }
        };
    }

}
