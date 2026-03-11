package com.aprimorar.api.domain.employee.exception;

import com.aprimorar.api.exception.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;

@RestControllerAdvice
@Slf4j
public class EmployeeServiceExceptionHandler {

    private final Clock applicationClock;

    public EmployeeServiceExceptionHandler(Clock applicationClock) {
        this.applicationClock = applicationClock;
    }


    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Object> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        ApiError apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                applicationClock.instant()
        );

        //TODO Tem como retirar essa duplicação do HTTP status aqui?
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

}
