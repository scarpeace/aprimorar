package aprimorar.financeiro.cobrancas_particular.config;

import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.cobrancas_particular.domain.exception.CobrancaParticularDadosInvalidosException;
import aprimorar.financeiro.cobrancas_particular.domain.exception.CobrancaParticularNaoEncontradaException;
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
@RestControllerAdvice(basePackages = {
    "aprimorar.agendamento.atendimentos_individuais.web",
    "aprimorar.agendamento.alunos.web",
    "aprimorar.financeiro.cobrancas_particular.web"
})
public class CobrancaParticularExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CobrancaParticularExceptionHandler.class);
    private static final String COBRANCA_NAO_ENCONTRADA = "COBRANCA_PARTICULAR_NAO_ENCONTRADA";
    private static final String COBRANCA_DADOS_INVALIDOS = "COBRANCA_PARTICULAR_DADOS_INVALIDOS";
    private static final String COBRANCA_JA_EXISTENTE = "COBRANCA_PARTICULAR_JA_EXISTENTE";
    private static final String COBRANCA_VALOR_INVALIDO = "COBRANCA_PARTICULAR_VALOR_INVALIDO";
    private static final String FINANCEIRO_CONFLITO_DE_DADOS = "FINANCEIRO_CONFLITO_DE_DADOS";

    @ExceptionHandler(CobrancaParticularNaoEncontradaException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
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
    public ResponseEntity<ProblemDetail> handleBadRequest(
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
                log.error("Erro de integridade dos dados da cobrança particular", ex);
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
