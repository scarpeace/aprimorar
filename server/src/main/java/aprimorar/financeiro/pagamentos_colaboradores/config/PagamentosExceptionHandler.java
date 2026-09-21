package aprimorar.financeiro.pagamentos_colaboradores.config;

import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.PagamentoDadosInvalidosException;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.PagamentoNaoEncontradoException;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.RepasseDadosInvalidosException;
import aprimorar.financeiro.pagamentos_colaboradores.domain.exception.RepasseNaoEncontradoException;
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
    basePackages = "aprimorar.financeiro.financeiro_particular.pagamentos_colaboradores.web"
)
public class PagamentosExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(
        PagamentosExceptionHandler.class
    );
    private static final String PAGAMENTO_NAO_ENCONTRADO =
        "PAGAMENTO_PARTICULAR_NAO_ENCONTRADO";
    private static final String PAGAMENTO_DADOS_INVALIDOS =
        "PAGAMENTO_PARTICULAR_DADOS_INVALIDOS";
    private static final String PAGAMENTO_VALOR_INVALIDO =
        "PAGAMENTO_PARTICULAR_VALOR_INVALIDO";
    private static final String PAGAMENTO_FORMA_INVALIDA =
        "PAGAMENTO_PARTICULAR_FORMA_PAGAMENTO_INVALIDA";
    private static final String REPASSE_NAO_ENCONTRADO =
        "REPASSE_PARTICULAR_NAO_ENCONTRADO";
    private static final String REPASSE_DADOS_INVALIDOS =
        "REPASSE_PARTICULAR_DADOS_INVALIDOS";
    private static final String REPASSE_JA_EXISTENTE =
        "REPASSE_PARTICULAR_JA_EXISTENTE";
    private static final String REPASSE_VALOR_INVALIDO =
        "REPASSE_PARTICULAR_VALOR_INVALIDO";
    private static final String FINANCEIRO_CONFLITO_DE_DADOS =
        "FINANCEIRO_CONFLITO_DE_DADOS";

    @ExceptionHandler(PagamentoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handlePagamentoNotFound(
        PagamentoNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.NOT_FOUND,
            PAGAMENTO_NAO_ENCONTRADO,
            ex.getMessage(),
            request
        );
    }

    @ExceptionHandler(PagamentoDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handlePagamentoBadRequest(
        PagamentoDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(
            HttpStatus.BAD_REQUEST,
            PAGAMENTO_DADOS_INVALIDOS,
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
            case "ck_pagamentos_particular_valor_total" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                PAGAMENTO_VALOR_INVALIDO,
                "O valor total do pagamento não pode ser negativo.",
                request
            );
            case "ck_pagamentos_particular_forma_pagamento" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                PAGAMENTO_FORMA_INVALIDA,
                "A forma de pagamento informada é inválida.",
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
            default -> {
                log.error("Erro de integridade dos pagamentos de colaboradores", ex);
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
