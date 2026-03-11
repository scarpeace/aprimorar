package com.aprimorar.api.domain.student;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.UpdateStudentDTO;

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

    @Operation(summary = "Create STUDENT", description = "Creates student with student, address and parent data")
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid StudentRequestDTO createStudentDto) {
        log.info("StudentController::createStudent request received");
        StudentResponseDTO response = studentService.createStudent(createStudentDto);
        log.info("StudentController::createStudent created studentId={}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "List all STUDENTS", description = "Retrieves all students from database with pagination")
    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> listStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<StudentResponseDTO> students = studentService.getStudents(pageRequest);

        return ResponseEntity.ok(students);
    }

    @Operation(summary = "List single STUDENT", description = "Retrieves single student based on ID")
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID studentId) {
        
        log.info("StudentController::getStudentById by id {}", studentId);
        StudentResponseDTO foundStudent = studentService.getById(studentId);
        
        log.info("StudentController::getStudentById completed for id={}", studentId);
        return ResponseEntity.ok(foundStudent);
    }

    @Operation(summary = "Update STUDENT", description = "Updates student with full student data")
    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody @Valid UpdateStudentDTO updateStudentDto) {
        
        log.info("StudentController::updateStudent started for id={}", studentId);
        StudentResponseDTO updatedStudent = studentService.updateStudent(studentId, updateStudentDto);
        
        log.info("StudentController::updateStudent completed for id={}", studentId);
        return ResponseEntity.ok(updatedStudent);
    }

    @Operation(summary = "Archive STUDENT (DELETE alias)", description = "Archives a single student based on ID using DELETE for REST compatibility")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> archiveStudentByDeleteAlias(@PathVariable UUID studentId) {
        
        log.info("StudentController::archiveStudentByDeleteAlias started for id={}", studentId);
        studentService.archiveStudent(studentId);
        
        log.info("StudentController::archiveStudentByDeleteAlias completed for id={}", studentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Archive STUDENT", description = "Archives a single student based on ID")
    @PatchMapping("/{studentId}/archive")
    public ResponseEntity<Void> archiveStudent(@PathVariable UUID studentId) {
        
        log.info("StudentController::archiveStudent started for id={}", studentId);
        studentService.archiveStudent(studentId);
        
        log.info("StudentController::archiveStudent completed for id={}", studentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Unarchive STUDENT", description = "Unarchives a single student based on ID")
    @PatchMapping("/{studentId}/unarchive")
    public ResponseEntity<Void> unarchiveStudent(@PathVariable UUID studentId) {
        
        log.info("StudentController::unarchiveStudent started for id={}", studentId);
        studentService.unarchiveStudent(studentId);
        
        log.info("StudentController::unarchiveStudent completed for id={}", studentId);
        return ResponseEntity.noContent().build();
    }

}
