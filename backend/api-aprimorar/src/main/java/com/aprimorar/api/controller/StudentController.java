package com.aprimorar.api.controller;

import com.aprimorar.api.controller.dto.StudentReponseDto;
import com.aprimorar.api.controller.dto.StudentRequestDto;
import com.aprimorar.api.service.StudentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/students")
@Tag(name = "Students", description = "Student management APIs")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping()
    public ResponseEntity<Page<StudentReponseDto>> listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<StudentReponseDto> allStudents = studentService.listStudents(pageable);

        return ResponseEntity.ok(allStudents);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<StudentReponseDto>> listActiveStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        Page<StudentReponseDto> allActiveStudents = studentService.listActiveStudents(pageable);

        return ResponseEntity.ok(allActiveStudents);

    }

    @GetMapping("/{studentId}")
    public ResponseEntity<StudentReponseDto> getStudentById(@PathVariable String studentId){
        var foundStudent = studentService.findById(studentId);

       return ResponseEntity.ok(foundStudent);
    }

    @PostMapping()
    public ResponseEntity<StudentReponseDto> createStudent(@RequestBody @Valid StudentRequestDto studentRequestDto){
        StudentReponseDto response = studentService.createStudent(studentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable String studentId){
        studentService.softDeleteStudent(studentId);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentReponseDto> updateStudent(
            @PathVariable String studentId ,
            @RequestBody @Valid StudentRequestDto studentRequestDto){

        StudentReponseDto updatedStudent = studentService.updateStudent(studentId, studentRequestDto);

        return ResponseEntity.ok(updatedStudent);
    }

}
