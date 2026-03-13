package com.aprimorar.api.domain.student;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/students")
@Tag(name = "Students", description = "Student management APIs")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Create STUDENT", description = "Creates student with student data, address, and parent reference")
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid StudentRequestDTO createStudentDto) {

        StudentResponseDTO response = studentService.createStudent(createStudentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all STUDENTS", description = "Retrieves all students from database with pagination")
    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> listStudents(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<StudentResponseDTO> students = studentService.getStudents(pageable);
        return ResponseEntity.ok(students);
    }

    @Operation(summary = "List single STUDENT", description = "Retrieves single student based on ID")
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID studentId) {
        
        StudentResponseDTO foundStudent = studentService.findById(studentId);
        return ResponseEntity.ok(foundStudent);
    }

    @Operation(summary = "Update STUDENT", description = "Fully updates student data and parent reference")
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody @Valid StudentRequestDTO studentRequestDto) {
        
        StudentResponseDTO updatedStudent = studentService.updateStudent(studentId, studentRequestDto);
        return ResponseEntity.ok(updatedStudent);
    }

    @Operation(summary = "Delete Student", description = "Deletes a student from the database")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId) {
        
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Archive STUDENT", description = "Archives a single student based on ID")
    @PatchMapping("/{studentId}/archive")
    public ResponseEntity<Void> archiveStudent(@PathVariable UUID studentId) {
        
        studentService.archiveStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unarchive STUDENT", description = "Unarchives a single student based on ID")
    @PatchMapping("/{studentId}/unarchive")
    public ResponseEntity<Void> unarchiveStudent(@PathVariable UUID studentId) {
        
        studentService.unarchiveStudent(studentId);
        return ResponseEntity.noContent().build();
    }

}
