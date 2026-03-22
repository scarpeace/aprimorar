package com.aprimorar.api.exception;

import java.net.URI;
import java.time.Clock;

import com.aprimorar.api.domain.address.exception.InvalidAddressException;
import com.aprimorar.api.domain.dashboard.exception.InvalidDashboardRequestException;
import com.aprimorar.api.domain.employee.exception.EmployeeAlreadyExistsException;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.event.exception.EventScheduleConflictException;
import com.aprimorar.api.domain.event.exception.InvalidEventException;
import com.aprimorar.api.domain.parent.exception.InvalidParentException;
import com.aprimorar.api.domain.parent.exception.ParentAlreadyExistsException;
import com.aprimorar.api.domain.parent.exception.ParentHasLinkedStudentsException;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.student.exception.StudentAlreadyExistException;
import com.aprimorar.api.domain.student.exception.InvalidStudentException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock applicationClock;

    public GlobalExceptionHandler(Clock applicationClock) {
        this.applicationClock = applicationClock;
    }

    @ExceptionHandler({
            EmployeeNotFoundException.class,
            EventNotFoundException.class,
            ParentNotFoundException.class,
            StudentNotFoundException.class
    })
    @ApiResponse(
            responseCode = "404",
            description = "Recurso não encontrado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    public ResponseEntity<ProblemDetail> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildProblemDetail(
                ErrorCode.RESOURCE_NOT_FOUND,
                HttpStatus.NOT_FOUND,
                "Recurso não encontrado",
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler({
            DataIntegrityViolationException.class,
            EmployeeAlreadyExistsException.class,
            EventScheduleConflictException.class,
            ParentHasLinkedStudentsException.class,
            ParentAlreadyExistsException.class,
            StudentAlreadyExistException.class
    })
    @ApiResponse(
            responseCode = "409",
            description = "Conflito de negócio",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    public ResponseEntity<ProblemDetail> handleConflictExceptions(
            RuntimeException ex,
            HttpServletRequest request) {
        return buildProblemDetail(
                ErrorCode.BUSINESS_ERROR,
                HttpStatus.CONFLICT,
                resolveConflictTitle(ex),
                ex.getMessage(),
                request
        );
    }

    @ExceptionHandler({
            InvalidDashboardRequestException.class,
            InvalidAddressException.class,
            InvalidParentException.class,
            InvalidEventException.class,
            InvalidStudentException.class,
            HttpMessageNotReadableException.class,
            MethodArgumentNotValidException.class
    })
    @ApiResponse(
            responseCode = "400",
            description = "Requisição inválida",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class))
    )
    public ResponseEntity<ProblemDetail> handleBadRequestExceptions(
            Exception ex,
            HttpServletRequest request) {
        return buildProblemDetail(
                ErrorCode.VALIDATION_ERROR,
                HttpStatus.BAD_REQUEST,
                resolveBadRequestTitle(ex),
                resolveBadRequestDetail(ex),
                request
        );
    }

    private String resolveConflictTitle(RuntimeException ex) {
        if (ex instanceof EventScheduleConflictException) {
            return "Conflito de agenda";
        }
        if (ex instanceof DataIntegrityViolationException) {
            return "Conflito de dados";
        }
        return "Conflito de negócio";
    }

    private String resolveBadRequestTitle(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException) {
            return "Falha de validação";
        }
        if (ex instanceof HttpMessageNotReadableException) {
            return "Corpo da requisição inválido";
        }
        return "Requisição inválida";
    }

    private String resolveBadRequestDetail(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException validationException) {
            return validationException.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .findFirst()
                    .orElse("Dados inválidos");
        }
        if (ex instanceof HttpMessageNotReadableException notReadableException) {
            return notReadableException.getMessage() != null
                    ? notReadableException.getMessage()
                    : "Corpo da requisição inválido";
        }
        return ex.getMessage();
    }

    private ResponseEntity<ProblemDetail> buildProblemDetail(
            ErrorCode errorCode,
            HttpStatus status,
            String title,
            String detail,
            HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("code", errorCode.name());
        problemDetail.setProperty("timestamp", applicationClock.instant().toString());

        return ResponseEntity.status(status).body(problemDetail);
    }
}
