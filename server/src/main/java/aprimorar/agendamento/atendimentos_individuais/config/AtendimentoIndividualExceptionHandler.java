package aprimorar.agendamento.atendimentos_individuais.config;

import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualEdicaoExpiradaException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.common.utils.ExceptionUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackages = "aprimorar.agendamento.atendimentos_individuais.web")
public class AtendimentoIndividualExceptionHandler {

    private static final String ATENDIMENTO_NAO_ENCONTRADO =
        "ATENDIMENTO_INDIVIDUAL_NAO_ENCONTRADO";
    private static final String ATENDIMENTO_CONFLITANTE =
        "ATENDIMENTO_INDIVIDUAL_CONFLITANTE";
    private static final String ATENDIMENTO_DADOS_INVALIDOS =
        "ATENDIMENTO_INDIVIDUAL_DADOS_INVALIDOS";
    private static final String ATENDIMENTO_EDICAO_EXPIRADA =
        "ATENDIMENTO_INDIVIDUAL_EDICAO_EXPIRADA";
    private static final String ATENDIMENTO_PERIODO_INVALIDO =
        "ATENDIMENTO_INDIVIDUAL_PERIODO_INVALIDO";
    private static final String ATENDIMENTO_STATUS_INVALIDO =
        "ATENDIMENTO_INDIVIDUAL_STATUS_INVALIDO";

    @ExceptionHandler(AtendimentoIndividualNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        AtendimentoIndividualNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            ATENDIMENTO_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(AtendimentoIndividualConflitanteException.class)
    public ResponseEntity<ProblemDetail> handleConflict(
        AtendimentoIndividualConflitanteException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.CONFLICT,
            ATENDIMENTO_CONFLITANTE,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(AtendimentoIndividualEdicaoExpiradaException.class)
    public ResponseEntity<ProblemDetail> handleExpiredEdit(
        AtendimentoIndividualEdicaoExpiradaException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            ATENDIMENTO_EDICAO_EXPIRADA,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(AtendimentoIndividualDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        AtendimentoIndividualDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            ATENDIMENTO_DADOS_INVALIDOS,
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
            case "ck_atendimentos_individuais_periodo" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                ATENDIMENTO_PERIODO_INVALIDO,
                "A data de fim deve ser posterior à data de início.",
                request
            );
            case "ck_atendimentos_individuais_status" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                ATENDIMENTO_STATUS_INVALIDO,
                "O status informado para o atendimento é inválido.",
                request
            );
            default -> throw ex;
        };
    }
}
