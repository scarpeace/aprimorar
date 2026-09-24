package aprimorar.financeiro.recebimentos_alunos.config;

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
@RestControllerAdvice(
    basePackages = "aprimorar.financeiro.recebimentos_alunos.web"
)
public class RecebimentosAlunosExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(
        RecebimentosAlunosExceptionHandler.class
    );
    private static final String RECEBIMENTO_VALOR_INVALIDO =
        "RECEBIMENTO_VALOR_INVALIDO";
    private static final String RECEBIMENTO_FORMA_INVALIDA =
        "RECEBIMENTO_FORMA_PAGAMENTO_INVALIDA";
    private static final String FINANCEIRO_CONFLITO_DE_DADOS =
        "FINANCEIRO_CONFLITO_DE_DADOS";

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        String constraint = ExceptionUtils.findConstraintName(ex);

        return switch (constraint == null ? "" : constraint) {
            case "ck_cobranca_recebimentos_valor_total" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                RECEBIMENTO_VALOR_INVALIDO,
                "O valor total do recebimento não pode ser negativo.",
                request
            );
            case "ck_cobranca_recebimentos_forma_pagamento" -> ExceptionUtils.response(
                HttpStatus.BAD_REQUEST,
                RECEBIMENTO_FORMA_INVALIDA,
                "A forma de pagamento informada é inválida.",
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
