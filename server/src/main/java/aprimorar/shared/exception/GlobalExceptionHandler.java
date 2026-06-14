package aprimorar.shared.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    @ApiResponse(
        responseCode = "400",
        description = "Erro de regra de negócio",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<ProblemResponseDTO> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        log.error("Erro de regra de negócio: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(new ProblemResponseDTO(
            ex.getStatus(),
            ex.getMessage(),
            request.getRequestURI()
        ));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ApiResponse(
        responseCode = "409",
        description = "Requisição inválida",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleConflictExceptions(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de conflito de dados: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(
        responseCode = "400",
        description = "Falha de validação",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getDefaultMessage())
            .findFirst()
            .orElse("Erro de validação nos campos informados");

        log.error("Erro de validação de DTO: {}", errorMessage);
        return new ProblemResponseDTO(
            HttpStatus.BAD_REQUEST,
            errorMessage,
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({ AuthenticationCredentialsNotFoundException.class, BadCredentialsException.class, DisabledException.class })
    @ApiResponse(
        responseCode = "401",
        description = "Não autenticado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleUnauthorizedExceptions(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de autenticação: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ApiResponse(
        responseCode = "400",
        description = "Corpo da requisição inválido",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleMalformedRequest(HttpMessageNotReadableException ex, HttpServletRequest request) {
        log.error("Erro de payload inválido: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.BAD_REQUEST,
            "Corpo da requisição inválido",
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handle(Exception ex, HttpServletRequest request) {
        log.error("Ocorreu um erro interno: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ex.getMessage(),
            request.getRequestURI()
        );
    }
}
