package com.aprimorar.api.controller;

import com.aprimorar.api.entity.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {

    @GetMapping()
    public List<Student> listStudents(){
        return null;
    }

}
