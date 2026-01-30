package com.aprimorar.api.controller;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
public class StudentController {


    @Autowired
    private StudentService studentService;

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

    @PostMapping("/new")
    public ResponseEntity<StudentReponseDto> createStudent(@RequestBody StudentRequestDto studentRequestDto){
        StudentReponseDto response = studentService.createStudent(studentRequestDto);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable String studentId){
        String response  = studentService.softDeleteStudent(studentId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<StudentReponseDto> updateStudent(
            @PathVariable String studentId ,
            @RequestBody StudentRequestDto studentRequestDto){

        StudentReponseDto updatedStudent = studentService.updateStudent(studentId, studentRequestDto);

        return ResponseEntity.ok(updatedStudent);
    }

}
