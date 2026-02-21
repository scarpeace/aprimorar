package com.aprimorar.api.controller;

import com.aprimorar.api.controller.dto.StudentResponseDto;
import com.aprimorar.api.controller.dto.CreateStudentDto;
import com.aprimorar.api.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/students")
@Tag(name = "Students", description = "Student management APIs")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "List all STUDENTS", description = "Retrieves all students from database with pagination")
    @GetMapping()
    public ResponseEntity<Page<StudentResponseDto>> listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<StudentResponseDto> allStudents = studentService.listStudents(pageable);
        return ResponseEntity.ok(allStudents);
    }

    @Operation(summary = "List all active STUDENTS", description = "Retrieves all ACTIVE students from database with pagination")
    @GetMapping("/active")
    public ResponseEntity<Page<StudentResponseDto>> listActiveStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<StudentResponseDto> allActiveStudents = studentService.listActiveStudents(pageable);
        return ResponseEntity.ok(allActiveStudents);

    }

    @Operation(summary = "List single STUDENT", description = "Retrieves single student based on ID")
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable UUID studentId){
        var foundStudent = studentService.findById(studentId);

       return ResponseEntity.ok(foundStudent);
    }

    @Operation(summary = "Create STUDENT", description = "Creates student with student, address and parent data")
    @PostMapping()
    public ResponseEntity<StudentResponseDto> createStudent(@RequestBody @Valid CreateStudentDto createStudentDto){
        StudentResponseDto response = studentService.createStudent(createStudentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Delete STUDENT", description = "Soft delete single student based on ID")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId){
        studentService.softDeleteStudent(studentId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update STUDENT", description = "Updates student with full student data")
    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentResponseDto> updateStudent(
            @PathVariable UUID studentId ,
            @RequestBody @Valid CreateStudentDto createStudentDto){

        StudentResponseDto updatedStudent = studentService.updateStudent(studentId, createStudentDto);

        return ResponseEntity.ok(updatedStudent);
    }

}
