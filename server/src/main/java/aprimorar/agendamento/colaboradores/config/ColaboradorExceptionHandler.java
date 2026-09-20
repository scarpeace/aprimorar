package aprimorar.agendamento.colaboradores.config;

import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorNaoEncontradoException;
import aprimorar.agendamento.colaboradores.domain.exception.ColaboradorPossuiRepassePendenteException;
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
@RestControllerAdvice(basePackages = "aprimorar.agendamento.colaboradores.web")
public class ColaboradorExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorExceptionHandler.class);
    private static final String COLABORADOR_NAO_ENCONTRADO = "COLABORADOR_NAO_ENCONTRADO";
    private static final String COLABORADOR_POSSUI_REPASSE_PENDENTE =
        "COLABORADOR_POSSUI_REPASSE_PENDENTE";
    private static final String COLABORADOR_CPF_DUPLICADO = "COLABORADOR_CPF_DUPLICADO";
    private static final String COLABORADOR_EMAIL_DUPLICADO = "COLABORADOR_EMAIL_DUPLICADO";
    private static final String COLABORADOR_CONFLITO_DE_DADOS = "COLABORADOR_CONFLITO_DE_DADOS";

    @ExceptionHandler(ColaboradorNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(ColaboradorNaoEncontradoException ex, HttpServletRequest request) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            COLABORADOR_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(ColaboradorPossuiRepassePendenteException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        ColaboradorPossuiRepassePendenteException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            COLABORADOR_POSSUI_REPASSE_PENDENTE,
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
            case "uk_colaboradores_cpf" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                COLABORADOR_CPF_DUPLICADO,
                "Já existe um colaborador com o CPF informado.",
                request
            );
            case "uk_colaboradores_email" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                COLABORADOR_EMAIL_DUPLICADO,
                "Já existe um colaborador com o e-mail informado.",
                request
            );
            default -> {
                log.error("Erro de integridade dos dados do colaborador", ex);
                yield ExceptionUtils.response(
                    HttpStatus.CONFLICT,
                    COLABORADOR_CONFLITO_DE_DADOS,
                    "Os dados informados violam uma restrição existente.",
                    request
                );
            }
        };
    }
}
