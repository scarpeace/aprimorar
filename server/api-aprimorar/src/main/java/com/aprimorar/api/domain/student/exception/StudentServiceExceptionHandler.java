package com.aprimorar.api.domain.student.exception;

import java.util.Collections;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aprimorar.api.domain.student.StudentEntity;
import com.aprimorar.api.requests.APIResponse;
import com.aprimorar.api.requests.ErrorDTO;

@RestControllerAdvice
public class StudentServiceExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public APIResponse<StudentEntity> handleStudentNotFoundException(StudentNotFoundException exception) {
        APIResponse<StudentEntity> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus("FAILED");
        serviceResponse.setErrors(Collections.singletonList(new ErrorDTO("", exception.getMessage())));

        return serviceResponse;
    }

}