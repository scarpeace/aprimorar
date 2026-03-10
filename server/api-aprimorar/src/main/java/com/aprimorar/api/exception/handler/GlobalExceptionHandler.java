package com.aprimorar.api.exception.handler;

import java.time.Clock;
import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.event.exception.EventNotFoundException;
import com.aprimorar.api.domain.parent.exception.ParentNotFoundException;
import com.aprimorar.api.domain.student.exception.StudentNotFoundException;
import com.aprimorar.api.exception.errors.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock applicationClock;

    public GlobalExceptionHandler(Clock applicationClock) {
        this.applicationClock = applicationClock;
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStudentNotFound(
            StudentNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = buildError(
                HttpStatus.NOT_FOUND,
                "STUDENT_NOT_FOUND",
                "Estudante não encontrado",
                request
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleUUIDNotValid(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = buildError(
                HttpStatus.BAD_REQUEST,
                "TYPE_MISMATCH",
                "Parâmetro de requisição inválido",
                request
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = buildError(
                HttpStatus.CONFLICT,
                "DATA_INTEGRITY_CONFLICT",
                "Conflito de integridade de dados",
                request
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ){
        ErrorResponse error = buildError(
                HttpStatus.BAD_REQUEST,
                "MALFORMED_REQUEST",
                "Corpo da requisição inválido",
                request
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFound(
            EventNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = buildError(
                HttpStatus.NOT_FOUND,
                "EVENT_NOT_FOUND",
                "Evento não encontrado",
                request
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEmployeeNotFound(
            EmployeeNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = buildError(
                HttpStatus.NOT_FOUND,
                "EMPLOYEE_NOT_FOUND",
                "Funcionário não encontrado",
                request
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleParentNotFound(
            ParentNotFoundException ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = buildError(
                HttpStatus.NOT_FOUND,
                "PARENT_NOT_FOUND",
                "Responsável não encontrado",
                request
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse error = buildError(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        ErrorResponse error = buildError(
                status,
                "REQUEST_ERROR",
                ex.getReason() == null ? "Requisição inválida" : ex.getReason(),
                request
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedError(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponse error = buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Erro interno do servidor",
                request
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ErrorResponse buildError(HttpStatus status, String code, String message, HttpServletRequest request) {
        return new ErrorResponse(
                Instant.now(applicationClock),
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                request.getRequestURI()
        );
    }

}
