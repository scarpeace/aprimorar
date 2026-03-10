package com.aprimorar.api.domain.employee.exception;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.exception.errors.ErrorResponse;
import com.aprimorar.api.requests.APIResponse;
import com.aprimorar.api.requests.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class EmployeeServiceExceptionHandler {

    @ExceptionHandler(EmployeeServiceBusinessException.class)
    public APIResponse<Employee> handleServiceException(EmployeeServiceBusinessException exception) {
        APIResponse<Employee> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return serviceResponse;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public APIResponse<Employee> handleEmployeeNotFoundException(EmployeeNotFoundException exception) {
        APIResponse<Employee> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return serviceResponse;
    }

}
