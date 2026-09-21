package aprimorar.financeiro.financeiro_particular.recebimentos_alunos.config;

import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularDadosInvalidosException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.api.CobrancaParticularNaoEncontradaException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.exception.RecebimentoParticularDadosInvalidosException;
import aprimorar.financeiro.financeiro_particular.recebimentos_alunos.domain.exception.RecebimentoParticularNaoEncontradoException;
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
@RestControllerAdvice(
    basePackages = "aprimorar.financeiro.financeiro_particular.recebimentos_alunos.web"
)
public class RecebimentosAlunosExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(
        RecebimentosAlunosExceptionHandler.class
    );
    private static final String RECEBIMENTO_NAO_ENCONTRADO =
        "RECEBIMENTO_PARTICULAR_NAO_ENCONTRADO";
    private static final String RECEBIMENTO_DADOS_INVALIDOS =
        "RECEBIMENTO_PARTICULAR_DADOS_INVALIDOS";
    private static final String RECEBIMENTO_VALOR_INVALIDO =
        "RECEBIMENTO_PARTICULAR_VALOR_INVALIDO";
    private static final String RECEBIMENTO_FORMA_INVALIDA =
        "RECEBIMENTO_PARTICULAR_FORMA_PAGAMENTO_INVALIDA";
    private static final String COBRANCA_NAO_ENCONTRADA =
        "COBRANCA_PARTICULAR_NAO_ENCONTRADA";
    private static final String COBRANCA_DADOS_INVALIDOS =
        "COBRANCA_PARTICULAR_DADOS_INVALIDOS";
    private static final String COBRANCA_JA_EXISTENTE =
        "COBRANCA_PARTICULAR_JA_EXISTENTE";
    private static final String COBRANCA_VALOR_INVALIDO =
        "COBRANCA_PARTICULAR_VALOR_INVALIDO";
    private static final String FINANCEIRO_CONFLITO_DE_DADOS =
        "FINANCEIRO_CONFLITO_DE_DADOS";

    @ExceptionHandler(RecebimentoParticularNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleRecebimentoNotFound(
        RecebimentoParticularNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            RECEBIMENTO_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(RecebimentoParticularDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleRecebimentoBadRequest(
        RecebimentoParticularDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            RECEBIMENTO_DADOS_INVALIDOS,
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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        String constraint = ExceptionUtils.findConstraintName(ex);

        return switch (constraint == null ? "" : constraint) {
            case "ck_recebimentos_particular_valor_total" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                RECEBIMENTO_VALOR_INVALIDO,
                "O valor total do recebimento não pode ser negativo.",
                request
            );
            case "ck_recebimentos_particular_forma_pagamento" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                RECEBIMENTO_FORMA_INVALIDA,
                "A forma de pagamento informada é inválida.",
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
            default -> {
                log.error("Erro de integridade dos recebimentos de alunos", ex);
                yield ExceptionUtils.response(
                    HttpStatus.CONFLICT,
                    FINANCEIRO_CONFLITO_DE_DADOS,
                    "Os dados informados violam uma restrição existente.",
                    request
                );
            }
        };
    }
}
