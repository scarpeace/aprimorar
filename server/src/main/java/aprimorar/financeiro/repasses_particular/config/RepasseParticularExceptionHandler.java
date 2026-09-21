package aprimorar.financeiro.repasses_particular.config;

import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularDadosInvalidosException;
import aprimorar.financeiro.repasses_particular.domain.exception.RepasseParticularNaoEncontradoException;
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
    "aprimorar.agendamento.atendimentos_particular.web",
    "aprimorar.financeiro.pagamentos_particular.web"
})
public class RepasseParticularExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RepasseParticularExceptionHandler.class);
    private static final String REPASSE_NAO_ENCONTRADO = "REPASSE_PARTICULAR_NAO_ENCONTRADO";
    private static final String REPASSE_DADOS_INVALIDOS = "REPASSE_PARTICULAR_DADOS_INVALIDOS";
    private static final String REPASSE_JA_EXISTENTE = "REPASSE_PARTICULAR_JA_EXISTENTE";
    private static final String REPASSE_VALOR_INVALIDO = "REPASSE_PARTICULAR_VALOR_INVALIDO";
    private static final String FINANCEIRO_CONFLITO_DE_DADOS = "FINANCEIRO_CONFLITO_DE_DADOS";

    @ExceptionHandler(RepasseParticularNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        RepasseParticularNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(HttpStatus.NOT_FOUND, REPASSE_NAO_ENCONTRADO, ex.getMessage(), request);
    }

    @ExceptionHandler(RepasseParticularDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        RepasseParticularDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(HttpStatus.BAD_REQUEST, REPASSE_DADOS_INVALIDOS, ex.getMessage(), request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        String constraint = ExceptionUtils.findConstraintName(ex);

        return switch (constraint == null ? "" : constraint) {
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
                log.error("Erro de integridade dos dados do repasse", ex);
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
