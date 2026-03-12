package com.aprimorar.api.domain.event.exception;

import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.exception.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;

@RestControllerAdvice
@Slf4j
public class EventServiceExceptionHandler {

    private final Clock applicationClock;

    public EventServiceExceptionHandler(Clock applicationClock) {
        this.applicationClock = applicationClock;
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Object> handleEmployeeNotFoundException(EventNotFoundException ex) {
        ApiError apiError = new ApiError(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                applicationClock.instant()
        );

        //TODO Tem como retirar essa duplicação do HTTP status aqui?
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    

}
