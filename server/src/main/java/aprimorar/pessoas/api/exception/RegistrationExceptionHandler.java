package aprimorar.pessoas.api.exception;

import aprimorar.pessoas.colaborador.api.exception.ColaboradorAlreadyExistsException;
import aprimorar.pessoas.colaborador.api.exception.ColaboradorBusinessException;
import aprimorar.pessoas.colaborador.api.exception.ColaboradorNotFoundException;
import aprimorar.pessoas.responsavel.api.exception.InvalidResponsavelException;
import aprimorar.pessoas.responsavel.api.exception.ResponsavelAlreadyExistsException;
import aprimorar.pessoas.responsavel.api.exception.ResponsavelHasLinkedStudentsException;
import aprimorar.pessoas.responsavel.api.exception.ResponsavelNotFoundException;
import aprimorar.pessoas.shared.address.exception.InvalidAddressException;
import aprimorar.pessoas.aluno.api.exception.AlunoAlreadyExistsException;
import aprimorar.pessoas.aluno.api.exception.AlunoNotFoundException;
import aprimorar.shared.exception.ProblemResponseDTO;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RegistrationExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(RegistrationExceptionHandler.class);

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
        ColaboradorNotFoundException.class,
        ResponsavelNotFoundException.class,
        AlunoNotFoundException.class
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
        ColaboradorAlreadyExistsException.class,
        PersonHasPendingFinancialsException.class,
        ResponsavelAlreadyExistsException.class,
        ResponsavelHasLinkedStudentsException.class,
        AlunoAlreadyExistsException.class
    })
    @ApiResponse(
        responseCode = "409",
        description = "Conflito de regra de negócio",
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
        InvalidResponsavelException.class
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

    @ExceptionHandler(ColaboradorBusinessException.class)
    @ApiResponse(
        responseCode = "409",
        description = "Erro de regra de negocio do modulo registration.pessoas.colaborador",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemResponseDTO.class))
    )
    public ResponseEntity<ProblemResponseDTO> handleEmployeeBusiness(
        ColaboradorBusinessException ex,
        HttpServletRequest request
    ) {
        log.error("Erro de regra de negocio em employee: {}", ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(new ProblemResponseDTO(
            ex.getStatus(),
            ex.getMessage(),
            request.getRequestURI()
        ));
    }
}
