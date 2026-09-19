package aprimorar.config;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String VALIDATION_ERROR_MESSAGE =
        "Erro de validação nos campos informados";

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleForbidden(AccessDeniedException ex, HttpServletRequest request) {
        return problem(
            HttpStatus.FORBIDDEN,
            "Acesso negado",
            "Você não possui permissão para realizar esta operação.",
            request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleConflict(
        DataIntegrityViolationException ex,
        HttpServletRequest request
    ) {
        log.error("Erro de integridade dos dados", ex);

        return problem(
            HttpStatus.CONFLICT,
            "Conflito de dados",
            "Os dados informados violam uma restrição existente.",
            request
        );
    }

    @ExceptionHandler({
        MethodArgumentNotValidException.class,
        BindException.class
    })
    public ProblemDetail handleValidation(
        Exception ex,
        HttpServletRequest request
    ) {
        BindingResult bindingResult = ex instanceof MethodArgumentNotValidException validation
            ? validation.getBindingResult()
            : ((BindException) ex).getBindingResult();

        List<String> errors = bindingResult.getAllErrors()
            .stream()
            .map(error -> error.getDefaultMessage() == null
                ? VALIDATION_ERROR_MESSAGE
                : error.getDefaultMessage())
            .toList();

        ProblemDetail problem = problem(
            HttpStatus.BAD_REQUEST,
            VALIDATION_ERROR_MESSAGE,
            VALIDATION_ERROR_MESSAGE,
            request
        );

        problem.setProperty(
            "errors",
            errors.isEmpty() ? List.of(VALIDATION_ERROR_MESSAGE) : errors
        );

        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMalformedRequest(HttpServletRequest request) {
        return problem(
            HttpStatus.BAD_REQUEST,
            "Corpo da requisição inválido",
            "Verifique o formato do JSON enviado.",
            request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(
        Exception ex,
        HttpServletRequest request
    ) {
        log.error("Erro interno não tratado", ex);

        return problem(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Erro interno do sistema",
            "Ocorreu um erro inesperado.",
            request
        );
    }

    private ProblemDetail problem(
        HttpStatus status,
        String title,
        String detail,
        HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));
        return problem;
    }
}
