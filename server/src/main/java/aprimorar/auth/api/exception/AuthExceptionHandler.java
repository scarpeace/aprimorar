package aprimorar.auth.api.exception;

import aprimorar.shared.exception.DomainBusinessException;
import aprimorar.shared.exception.ProblemResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AuthExceptionHandler.class);

    @ExceptionHandler(DomainBusinessException.class)
    @ApiResponse(
        responseCode = "400",
        description = "Erro de regra de negocio do modulo auth",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleDomainBusiness(DomainBusinessException ex, HttpServletRequest request) {
        log.error("Erro de regra de negocio: {}", ex.getMessage());
        return new ProblemResponseDTO(
            ex.getStatus(),
            ex.getMessage(),
            request.getRequestURI()
        );
    }
}
