package aprimorar.agendamento.atendimentos_individuais.config;

import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualConflitanteException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualDadosInvalidosException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualEdicaoExpiradaException;
import aprimorar.agendamento.atendimentos_individuais.domain.exception.AtendimentoIndividualNaoEncontradoException;
import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaNaoEncontradaException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseDadosInvalidosException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseNaoEncontradoException;
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
    private static final String COBRANCA_NAO_ENCONTRADA =
        "COBRANCA_PARTICULAR_NAO_ENCONTRADA";
    private static final String COBRANCA_DADOS_INVALIDOS =
        "COBRANCA_PARTICULAR_DADOS_INVALIDOS";
    private static final String COBRANCA_JA_EXISTENTE =
        "COBRANCA_PARTICULAR_JA_EXISTENTE";
    private static final String COBRANCA_VALOR_INVALIDO =
        "COBRANCA_PARTICULAR_VALOR_INVALIDO";
    private static final String REPASSE_NAO_ENCONTRADO =
        "REPASSE_PARTICULAR_NAO_ENCONTRADO";
    private static final String REPASSE_DADOS_INVALIDOS =
        "REPASSE_PARTICULAR_DADOS_INVALIDOS";
    private static final String REPASSE_JA_EXISTENTE =
        "REPASSE_PARTICULAR_JA_EXISTENTE";
    private static final String REPASSE_VALOR_INVALIDO =
        "REPASSE_PARTICULAR_VALOR_INVALIDO";

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

    @ExceptionHandler(CobrancaNaoEncontradaException.class)
    public ResponseEntity<ProblemDetail> handleCobrancaNotFound(
        CobrancaNaoEncontradaException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            COBRANCA_NAO_ENCONTRADA,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(CobrancaDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleCobrancaBadRequest(
        CobrancaDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            COBRANCA_DADOS_INVALIDOS,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(RepasseNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleRepasseNotFound(
        RepasseNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            REPASSE_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(RepasseDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleRepasseBadRequest(
        RepasseDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            REPASSE_DADOS_INVALIDOS,
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
            case "uk_cobrancas_atendimento" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                COBRANCA_JA_EXISTENTE,
                "Já existe uma cobrança para o atendimento informado.",
                request
            );
            case "ck_cobrancas_valor" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                COBRANCA_VALOR_INVALIDO,
                "O valor da cobrança não pode ser negativo.",
                request
            );
            case "uk_repasses_atendimento" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                REPASSE_JA_EXISTENTE,
                "Já existe um repasse para o atendimento informado.",
                request
            );
            case "ck_repasses_valor" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                REPASSE_VALOR_INVALIDO,
                "O valor do repasse não pode ser negativo.",
                request
            );
            default -> throw ex;
        };
    }
}
