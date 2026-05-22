package aprimorar.appointment.api.exception;

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
public class AppointmentExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(AppointmentExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AppointmentNotFoundException.class)
    @ApiResponse(
        responseCode = "404",
        description = "Recurso não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleNotFound(AppointmentNotFoundException ex, HttpServletRequest request) {
        log.error("Erro de Recurso não encontrado: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(AppointmentScheduleConflictException.class)
    @ApiResponse(
        responseCode = "400",
        description = "Requisição inválida",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleConflict(AppointmentScheduleConflictException ex, HttpServletRequest request) {
        log.error("Erro de conflito de dados: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ InvalidAppointmentException.class, NotAllowedToUpdateAppointmentException.class })
    @ApiResponse(
        responseCode = "403",
        description = "Operação não permitida",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleBadRequest(Exception ex, HttpServletRequest request) {
        log.error("Erro de validação de entidade: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.FORBIDDEN,
            ex.getMessage(),
            request.getRequestURI()
        );
    }
}
