package com.aprimorar.api.domain.parent.exception;

import com.aprimorar.api.domain.employee.Employee;
import com.aprimorar.api.domain.employee.exception.EmployeeNotFoundException;
import com.aprimorar.api.domain.employee.exception.EmployeeServiceBusinessException;
import com.aprimorar.api.requests.APIResponse;
import com.aprimorar.api.requests.ErrorDTO;
import org.hibernate.annotations.Parent;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;

@RestControllerAdvice
public class ParentServiceExceptionHandler {

    @ExceptionHandler(EmployeeServiceBusinessException.class)
    public APIResponse<Employee> handleServiceException(EmployeeServiceBusinessException exception) {
        APIResponse<Employee> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return serviceResponse;
    }

    @ExceptionHandler(ParentNotFoundException.class)
    public APIResponse<Parent> handleEmployeeNotFoundException(ParentNotFoundException exception) {
        APIResponse<Parent> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return serviceResponse;
    }

}
