package com.aprimorar.api.domain.employee.exception;

import com.aprimorar.api.requests.APIResponse;
import com.aprimorar.api.requests.ErrorDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class EmployeeServiceExceptionHandler {
    @ExceptionHandler(EmployeeServiceBusinessException.class)
    public APIResponse<?> handleServiceException(EmployeeServiceBusinessException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return serviceResponse;
    }
}
