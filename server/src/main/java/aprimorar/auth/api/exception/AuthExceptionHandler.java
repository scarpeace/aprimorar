package aprimorar.auth.api.exception;

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
public class AuthExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({ AdminUserCannotBeChangedException.class, AdminUserAlreadyExistsException.class })
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negocio para usuario ADMIN",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleConflict(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de conflito de dados: {}", ex.getMessage());
        return new ProblemResponseDTO(
            ErrorCode.CONFLICT,
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }
}
