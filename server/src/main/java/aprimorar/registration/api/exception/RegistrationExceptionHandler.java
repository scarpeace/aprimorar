package aprimorar.registration.api.exception;

import aprimorar.registration.employee.api.exception.EmployeeAlreadyExistsException;
import aprimorar.registration.employee.api.exception.EmployeeNotFoundException;
import aprimorar.registration.parent.api.exception.InvalidParentException;
import aprimorar.registration.parent.api.exception.ParentAlreadyExistsException;
import aprimorar.registration.parent.api.exception.ParentHasLinkedStudentsException;
import aprimorar.registration.parent.api.exception.ParentNotFoundException;
import aprimorar.registration.shared.address.exception.InvalidAddressException;
import aprimorar.registration.student.api.exception.StudentAlreadyExistException;
import aprimorar.registration.student.api.exception.StudentNotFoundException;
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
public class RegistrationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RegistrationExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
        EmployeeNotFoundException.class,
        ParentNotFoundException.class,
        StudentNotFoundException.class
    })
    @ApiResponse(
        responseCode = "404",
        description = "Recurso não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleNotFound(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de Recurso não encontrado: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({
        EmployeeAlreadyExistsException.class,
        PersonHasPendingFinancialsException.class,
        ParentAlreadyExistsException.class,
        ParentHasLinkedStudentsException.class,
        StudentAlreadyExistException.class
    })
    @ApiResponse(
        responseCode = "400",
        description = "Requisição inválida",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ProblemResponseDTO handleConflict(RuntimeException ex, HttpServletRequest request) {
        log.error("Erro de conflito de dados: {}", ex.getMessage());
        return new ProblemResponseDTO(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request.getRequestURI()
        );
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
        InvalidAddressException.class,
        InvalidParentException.class
    })
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
