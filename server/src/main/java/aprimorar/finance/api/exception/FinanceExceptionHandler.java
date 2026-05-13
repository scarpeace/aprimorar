package aprimorar.finance.api.exception;

import aprimorar.shared.exception.ErrorCode;
import aprimorar.shared.exception.ProblemResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FinanceExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(FinanceExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TransactionNotFoundException.class)
    @ApiResponse(
        responseCode = "404",
        description = "Recurso não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleNotFound(TransactionNotFoundException ex, HttpServletRequest request) {
        log.error("Erro de Recurso não encontrado: {}", ex.getMessage());
        return new ProblemResponseDTO(
            ErrorCode.RESOURCE_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI()
        );
    }
}
