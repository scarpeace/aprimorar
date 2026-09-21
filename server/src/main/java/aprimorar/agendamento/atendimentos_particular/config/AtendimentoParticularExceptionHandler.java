package aprimorar.agendamento.atendimentos_particular.config;

import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularConflitanteException;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularDadosInvalidosException;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularEdicaoExpiradaException;
import aprimorar.agendamento.atendimentos_particular.domain.exception.AtendimentoParticularNaoEncontradoException;
import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.RepasseParticularDadosInvalidosException;
import aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.api.RepasseParticularNaoEncontradoException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularDadosInvalidosException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularNaoEncontradaException;
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
@RestControllerAdvice(basePackages = "aprimorar.agendamento.atendimentos_particular.web")
public class AtendimentoParticularExceptionHandler {

    private static final String ATENDIMENTO_NAO_ENCONTRADO =
        "ATENDIMENTO_PARTICULAR_NAO_ENCONTRADO";
    private static final String ATENDIMENTO_CONFLITANTE =
        "ATENDIMENTO_PARTICULAR_CONFLITANTE";
    private static final String ATENDIMENTO_DADOS_INVALIDOS =
        "ATENDIMENTO_PARTICULAR_DADOS_INVALIDOS";
    private static final String ATENDIMENTO_EDICAO_EXPIRADA =
        "ATENDIMENTO_PARTICULAR_EDICAO_EXPIRADA";
    private static final String ATENDIMENTO_PERIODO_INVALIDO =
        "ATENDIMENTO_PARTICULAR_PERIODO_INVALIDO";
    private static final String ATENDIMENTO_STATUS_INVALIDO =
        "ATENDIMENTO_PARTICULAR_STATUS_INVALIDO";
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

    @ExceptionHandler(AtendimentoParticularNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        AtendimentoParticularNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            ATENDIMENTO_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(AtendimentoParticularConflitanteException.class)
    public ResponseEntity<ProblemDetail> handleConflict(
        AtendimentoParticularConflitanteException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.CONFLICT,
            ATENDIMENTO_CONFLITANTE,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(AtendimentoParticularEdicaoExpiradaException.class)
    public ResponseEntity<ProblemDetail> handleExpiredEdit(
        AtendimentoParticularEdicaoExpiradaException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            ATENDIMENTO_EDICAO_EXPIRADA,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(AtendimentoParticularDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        AtendimentoParticularDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            ATENDIMENTO_DADOS_INVALIDOS,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(CobrancaParticularNaoEncontradaException.class)
    public ResponseEntity<ProblemDetail> handleCobrancaNotFound(
        CobrancaParticularNaoEncontradaException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            COBRANCA_NAO_ENCONTRADA,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(CobrancaParticularDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleCobrancaBadRequest(
        CobrancaParticularDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            COBRANCA_DADOS_INVALIDOS,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(RepasseParticularNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleRepasseNotFound(
        RepasseParticularNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            REPASSE_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(RepasseParticularDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleRepasseBadRequest(
        RepasseParticularDadosInvalidosException ex,
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
            case "ck_atendimentos_particular_periodo" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                ATENDIMENTO_PERIODO_INVALIDO,
                "A data de fim deve ser posterior à data de início.",
                request
            );
            case "ck_atendimentos_particular_status" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                ATENDIMENTO_STATUS_INVALIDO,
                "O status informado para o atendimento é inválido.",
                request
            );
            case "uk_cobrancas_particular_atendimento" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                COBRANCA_JA_EXISTENTE,
                "Já existe uma cobrança para o atendimento informado.",
                request
            );
            case "ck_cobrancas_particular_valor" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                COBRANCA_VALOR_INVALIDO,
                "O valor da cobrança não pode ser negativo.",
                request
            );
            case "uk_repasses_particular_atendimento" -> ExceptionUtils.response(
                HttpStatus.CONFLICT,
                REPASSE_JA_EXISTENTE,
                "Já existe um repasse para o atendimento informado.",
                request
            );
            case "ck_repasses_particular_valor" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                REPASSE_VALOR_INVALIDO,
                "O valor do repasse não pode ser negativo.",
                request
            );
            default -> throw ex;
        };
    }
}
