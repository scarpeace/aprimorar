package com.aprimorar.api.domain.student;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.student.dto.CreateStudentDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.domain.student.dto.UpdateStudentDTO;
import com.aprimorar.api.requests.common.PageQuery;
import com.aprimorar.api.shared.PageableUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/students")
@Tag(name = "Students", description = "Student management APIs")
public class StudentController {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("name", "createdAt");

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "List all STUDENTS", description = "Retrieves all students from database with pagination")
    @GetMapping()
    public ResponseEntity<Page<StudentResponseDTO>> listStudents(
            @Valid @ModelAttribute PageQuery pageQuery,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "false") boolean includeArchived
    ) {
        Pageable pageable = PageableUtils.buildPageable(pageQuery, "name", ALLOWED_SORT_FIELDS);
        Page<StudentResponseDTO> allStudents = studentService.listStudents(pageable, name, includeArchived);
        return ResponseEntity.ok(allStudents);
    }

    @Operation(summary = "List single STUDENT", description = "Retrieves single student based on ID")
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID studentId) {
        var foundStudent = studentService.findById(studentId);

        return ResponseEntity.ok(foundStudent);
    }

    @Operation(summary = "Create STUDENT", description = "Creates student with student, address and parent data")
    @PostMapping()
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid CreateStudentDTO createStudentDto) {
        StudentResponseDTO response = studentService.createStudent(createStudentDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Archive STUDENT (DELETE alias)", description = "Archives a single student based on ID using DELETE for REST compatibility")
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> archiveStudentByDeleteAlias(@PathVariable UUID studentId) {
        studentService.archiveStudent(studentId);

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

    @Operation(summary = "Update STUDENT", description = "Updates student with full student data")
    @PatchMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody @Valid UpdateStudentDTO updateStudentDto) {

        StudentResponseDTO updatedStudent = studentService.updateStudent(studentId, updateStudentDto);

        return ResponseEntity.ok(updatedStudent);
    }

}
