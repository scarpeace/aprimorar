package com.aprimorar.api.controller;

import com.aprimorar.api.dto.student.CreateStudentDTO;
import com.aprimorar.api.dto.student.StudentResponseDTO;
import com.aprimorar.api.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    @GetMapping()
    public ResponseEntity<Page<StudentResponseDTO>> listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") @Max(100) int size,
            @RequestParam(defaultValue = "name") String sortBy
    ){

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<StudentResponseDTO> allStudents = studentService.listStudents(pageable);
        return ResponseEntity.ok(allStudents);
    }

    @Operation(summary = "List all active STUDENTS", description = "Retrieves all ACTIVE students from database with pagination")
    @Transactional(readOnly = true)
    @GetMapping("/active")
    public ResponseEntity<Page<StudentResponseDTO>> listActiveStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") @Max(100) int size,
            @RequestParam(defaultValue = "name") String sortBy
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<StudentResponseDTO> allActiveStudents = studentService.listActiveStudents(pageable);
        return ResponseEntity.ok(allActiveStudents);

    }

    @Operation(summary = "List single STUDENT", description = "Retrieves single student based on ID")
    @Transactional(readOnly = true)
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID studentId){
        var foundStudent = studentService.findById(studentId);

       return ResponseEntity.ok(foundStudent);
    }

    @Operation(summary = "Create STUDENT", description = "Creates student with student, address and parent data")
    @PostMapping()
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid CreateStudentDTO createStudentDto){
        StudentResponseDTO response = studentService.createStudent(createStudentDto);

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
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID studentId ,
            @RequestBody @Valid CreateStudentDTO createStudentDto){

        StudentResponseDTO updatedStudent = studentService.updateStudent(studentId, createStudentDto);

        return ResponseEntity.ok(updatedStudent);
    }

}
