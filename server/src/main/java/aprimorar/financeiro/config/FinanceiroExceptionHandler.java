package aprimorar.financeiro.config;

import aprimorar.common.utils.ExceptionUtils;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaJaExistenteException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.CobrancaNaoEncontradaException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.RecebimentoDadosInvalidosException;
import aprimorar.financeiro.recebimentos_alunos.domain.exception.RecebimentoNaoEncontradoException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.PagamentoDadosInvalidosException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.PagamentoNaoEncontradoException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseDadosInvalidosException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseJaExistenteException;
import aprimorar.financeiro.repasses_colaboradores.domain.exception.RepasseNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class FinanceiroExceptionHandler {

    private static final String RECEBIMENTO_NAO_ENCONTRADO =
        "RECEBIMENTO_NAO_ENCONTRADO";
    private static final String RECEBIMENTO_DADOS_INVALIDOS =
        "RECEBIMENTO_DADOS_INVALIDOS";
    private static final String COBRANCA_NAO_ENCONTRADA =
        "COBRANCA_NAO_ENCONTRADA";
    private static final String COBRANCA_DADOS_INVALIDOS =
        "COBRANCA_DADOS_INVALIDOS";
    private static final String COBRANCA_JA_EXISTENTE =
        "COBRANCA_JA_EXISTENTE";
    private static final String PAGAMENTO_NAO_ENCONTRADO =
        "PAGAMENTO_NAO_ENCONTRADO";
    private static final String PAGAMENTO_DADOS_INVALIDOS =
        "PAGAMENTO_DADOS_INVALIDOS";
    private static final String REPASSE_NAO_ENCONTRADO =
        "REPASSE_NAO_ENCONTRADO";
    private static final String REPASSE_DADOS_INVALIDOS =
        "REPASSE_DADOS_INVALIDOS";
    private static final String REPASSE_JA_EXISTENTE =
        "REPASSE_JA_EXISTENTE";

    @ExceptionHandler(RecebimentoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleRecebimentoNotFound(
        RecebimentoNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, RECEBIMENTO_NAO_ENCONTRADO, ex, request);
    }

    @ExceptionHandler(RecebimentoDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleRecebimentoBadRequest(
        RecebimentoDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, RECEBIMENTO_DADOS_INVALIDOS, ex, request);
    }

    @ExceptionHandler(CobrancaNaoEncontradaException.class)
    public ResponseEntity<ProblemDetail> handleCobrancaNotFound(
        CobrancaNaoEncontradaException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, COBRANCA_NAO_ENCONTRADA, ex, request);
    }

    @ExceptionHandler(CobrancaDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleCobrancaBadRequest(
        CobrancaDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, COBRANCA_DADOS_INVALIDOS, ex, request);
    }

    @ExceptionHandler(CobrancaJaExistenteException.class)
    public ResponseEntity<ProblemDetail> handleCobrancaConflict(
        CobrancaJaExistenteException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.CONFLICT, COBRANCA_JA_EXISTENTE, ex, request);
    }

    @ExceptionHandler(PagamentoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handlePagamentoNotFound(
        PagamentoNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, PAGAMENTO_NAO_ENCONTRADO, ex, request);
    }

    @ExceptionHandler(PagamentoDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handlePagamentoBadRequest(
        PagamentoDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, PAGAMENTO_DADOS_INVALIDOS, ex, request);
    }

    @ExceptionHandler(RepasseNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> handleRepasseNotFound(
        RepasseNaoEncontradoException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.NOT_FOUND, REPASSE_NAO_ENCONTRADO, ex, request);
    }

    @ExceptionHandler(RepasseDadosInvalidosException.class)
    public ResponseEntity<ProblemDetail> handleRepasseBadRequest(
        RepasseDadosInvalidosException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.BAD_REQUEST, REPASSE_DADOS_INVALIDOS, ex, request);
    }

    @ExceptionHandler(RepasseJaExistenteException.class)
    public ResponseEntity<ProblemDetail> handleRepasseConflict(
        RepasseJaExistenteException ex,
        HttpServletRequest request
    ) {
        return response(HttpStatus.CONFLICT, REPASSE_JA_EXISTENTE, ex, request);
    }

    private ResponseEntity<ProblemDetail> response(
        HttpStatus status,
        String code,
        RuntimeException exception,
        HttpServletRequest request
    ) {
        return ExceptionUtils.response(status, code, exception.getMessage(), request);
    }
}
