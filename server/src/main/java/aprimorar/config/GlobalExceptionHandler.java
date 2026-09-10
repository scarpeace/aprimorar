package aprimorar.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String VALIDATION_ERROR_MESSAGE = "Erro de validação nos campos informados";

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleConflictExceptions(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de conflito de dados: {}", ex.getMessage());
        return response(HttpStatus.CONFLICT, "Erro de conflito de dados", ex.getMessage(), request);
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, BindException.class })
    public ResponseEntity<ProblemDetail> handleValidationExceptions(Exception ex, HttpServletRequest request) {
        BindingResult bindingResult = ex instanceof MethodArgumentNotValidException validationException
            ? validationException.getBindingResult()
            : ((BindException) ex).getBindingResult();

        List<String> errorMessages = bindingResult.getAllErrors().stream()
            .map(error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : VALIDATION_ERROR_MESSAGE)
            .toList();

        if (errorMessages.isEmpty()) {
            errorMessages = List.of(VALIDATION_ERROR_MESSAGE);
        }

        log.error("Erro de validação de DTO: {}", errorMessages);
        ProblemDetail response = response(
            HttpStatus.BAD_REQUEST,
            VALIDATION_ERROR_MESSAGE,
            VALIDATION_ERROR_MESSAGE,
            request
        ).getBody();
        response.setProperty("errors", errorMessages);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleMalformedRequest(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Erro de payload inválido: {}", ex.getMessage());
        return response(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handle(Exception ex, HttpServletRequest request) {
        log.error("Ocorreu um erro interno: {}", ex.getMessage());
        return response(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do sistema", ex.getMessage(), request);
    }

    private ResponseEntity<ProblemDetail> response(
        HttpStatus status,
        String title,
        String detail,
        HttpServletRequest request
    ) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            status,
            detail == null ? status.getReasonPhrase() : detail
        );
        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(status).body(problem);
    }
}
