package com.aprimorar.api.exception;

import java.time.Clock;
import java.util.Locale;

import com.aprimorar.api.domain.address.exception.InvalidAddressException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
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

    /*
      ------------------------------------------------------------------------
      DOMAIN EXCEPTIONS
      ------------------------------------------------------------------------
     */

    @ExceptionHandler({
            EmployeeNotFoundException.class,
            EventNotFoundException.class,
            ParentNotFoundException.class,
            StudentNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({
            EventScheduleConflictException.class,
            ParentAlreadyExistsException.class,
            ParentHasLinkedStudentsException.class,
            EmployeeAlreadyExistsException.class,
            StudentAlreadyExistException.class,
            DataIntegrityViolationException.class
    })
    public ResponseEntity<ApiError> handleConflictExceptions(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler({
            InvalidEventException.class,
            InvalidParentException.class,
            InvalidStudentException.class,
            InvalidAddressException.class
    })
    public ResponseEntity<ApiError> handleInvalidDomainException(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    /*
      ------------------------------------------------------------------------
      FRAMEWORK / REQUEST EXCEPTIONS
      ------------------------------------------------------------------------
     */

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return buildErrorResponse(
                "MALFORMED_REQUEST_BODY",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        String objectName = ex.getBindingResult().getObjectName();
        String validationMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("Dados inválidos");

        String message = "Falha de validação em '" + objectName + "': " + validationMessage;

        return buildErrorResponse("VALIDATION_ERROR", message, HttpStatus.BAD_REQUEST, request);
    }


    /*
      ------------------------------------------------------------------------
      SHARED RESPONSE BUILDER
      ------------------------------------------------------------------------
     */

    private ResponseEntity<ApiError> buildErrorResponse(RuntimeException ex, HttpStatus status, HttpServletRequest request) {
        return buildErrorResponse(resolveErrorCode(ex), ex.getMessage(), status, request);
    }

    private ResponseEntity<ApiError> buildErrorResponse(
            String code,
            String message,
            HttpStatus status,
            HttpServletRequest request
    ) {
        ApiError apiError = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                code,
                message,
                request.getRequestURI(),
                applicationClock.instant()
        );

        return ResponseEntity.status(status).body(apiError);
    }

    private String resolveErrorCode(RuntimeException ex) {
        String simpleName = ex.getClass().getSimpleName();
        String baseName = simpleName.endsWith("Exception")
                ? simpleName.substring(0, simpleName.length() - "Exception".length())
                : simpleName;

        return baseName
                .replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                .toUpperCase(Locale.ROOT);
    }

}
