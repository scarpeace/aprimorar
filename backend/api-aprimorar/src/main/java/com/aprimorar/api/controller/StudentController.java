package com.aprimorar.api.controller;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {


    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public ResponseEntity<List<StudentReponseDto>> listStudents(){
        List<StudentReponseDto> allStudents = studentService.listStudents();

        return ResponseEntity.ok(allStudents);
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentReponseDto> getStudentById(@PathVariable String studentId){
        var foundStudent = studentService.findById(studentId);

       return ResponseEntity.ok(foundStudent);
    }

//    @PostMapping("/new")
//    public ResponseEntity<StudentReponseDto> createStudent(@RequestBody StudentRequestDto studentRequestDto){
//        StudentReponseDto response = studentService.createStudent(studentRequestDto);
//    }

}
