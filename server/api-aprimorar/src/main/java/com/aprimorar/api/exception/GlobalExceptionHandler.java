package com.aprimorar.api.exception;

import com.aprimorar.api.domain.address.exception.InvalidAddressException;
import com.aprimorar.api.domain.employee.exception.EmployeeAlreadyExistsException;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.event.exception.EventScheduleConflictException;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.event.exception.NotAllowedToUpdateEventException;
import com.aprimorar.api.domain.parent.exception.InvalidParentException;
import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.exception.ParentHasLinkedStudentsException;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

    @SuppressWarnings("unused")
    private Clock applicationClock;

    public GlobalExceptionHandler(Clock applicationClock) {
        this.applicationClock = applicationClock;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(
        {
            EmployeeNotFoundException.class,
            EventNotFoundException.class,
            ParentNotFoundException.class,
            StudentNotFoundException.class,
        }
    )
    @ApiResponse(
        responseCode = "404",
        description = "Recurso não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de Recurso não encontrado: {}", ex.getMessage());
        return new ProblemResponseDTO(
            ErrorCode.RESOURCE_NOT_FOUND,
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(
        {
            DataIntegrityViolationException.class,
            EmployeeAlreadyExistsException.class,
            EventScheduleConflictException.class,
            ParentHasLinkedStudentsException.class,
            ParentAlreadyExistsException.class,
            StudentAlreadyExistException.class,
        }
    )
    @ApiResponse(
        responseCode = "400",
        description = "Requisição inválida",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleConflictExceptions(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de conflito de dados: {}", ex.getMessage());
        return new ProblemResponseDTO(
            ErrorCode.CONFLICT,
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(
        {
            InvalidAddressException.class,
            InvalidParentException.class,
            InvalidEventException.class,
            InvalidAddressException.class,
            InvalidParentException.class,
            NotAllowedToUpdateEventException.class
        }
    )
    @ApiResponse(
        responseCode = "403",
        description = "Operação não permitida",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleBadRequestExceptions(Exception ex, HttpServletRequest request) {
        log.error("Erro de validação de entidade: {}", ex.getMessage());
        return new ProblemResponseDTO(
            ErrorCode.BUSINESS_ERROR,
            HttpStatus.FORBIDDEN,
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
            ErrorCode.VALIDATION_ERROR,
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
            ErrorCode.UNAUTHORIZED,
            HttpStatus.UNAUTHORIZED,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ HttpMessageNotReadableException.class, Exception.class })
    @ApiResponse(
        responseCode = "500",
        description = "Erro interno do sistema",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handle(Exception ex, HttpServletRequest request) {
        log.error("Ocorreu um erro interno: {}", ex.getMessage());
        return new ProblemResponseDTO(
            ErrorCode.VALIDATION_ERROR,
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Um erro interno ocorreu, contate o suporte ou tente novamente mais tarde",
            request.getRequestURI()
        );
    }
}
