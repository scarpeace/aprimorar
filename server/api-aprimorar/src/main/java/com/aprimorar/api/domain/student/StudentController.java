package com.aprimorar.api.domain.student;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.student.dto.StudentOptionDTO;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/students")
@Tag(name = "Student", description = "Student management APIs")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(
        operationId = "createStudent",
        summary = "Criar aluno",
        description = "Cria um novo aluno com os dados fornecidos."
    )
    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid StudentRequestDTO createStudentDto) {

        StudentResponseDTO response = studentService.createStudent(createStudentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        operationId = "getStudents",
        summary = "Listar alunos",
        description = "Retorna todos os alunos do banco de dados com paginação."
    )
    @GetMapping
    public ResponseEntity<Page<StudentResponseDTO>> listStudents(
            @PageableDefault(page = 0, size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String search
    ) {
        Page<StudentResponseDTO> students = studentService.getStudents(pageable, search);
        return ResponseEntity.ok(students);
    }

    @Operation(
        operationId = "getStudentOptions",
        summary = "Listar alunos para opções e dropdown",
        description = "Lista todos os alunos para uso em opções e dropdowns."
    )
    @GetMapping("/options")
    public ResponseEntity<List<StudentOptionDTO>> getStudentOptions() {
        List<StudentOptionDTO> options = studentService.getStudentOptions();
        return ResponseEntity.ok(options);
    }

    @Operation(
        operationId = "listStudentsByParent",
        summary = "Listar alunos por pai",
        description = "Retorna todos os alunos do banco de dados com paginação."
    )
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<StudentResponseDTO>> listStudentsByParent(
        @PathVariable UUID parentId
    ) {
        List<StudentResponseDTO> students = studentService.getStudentsByParent(parentId);
        return ResponseEntity.ok(students);
    }

    @Operation(
        operationId = "getStudentById",
        summary = "Listar aluno por ID",
        description = "Retorna um aluno específico com base no ID."
    )
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID studentId) {

        StudentResponseDTO foundStudent = studentService.findById(studentId);
        return ResponseEntity.ok(foundStudent);
    }

    @Operation(
        operationId = "updateStudent",
        summary = "Atualizar aluno",
        description = "Atualiza totalmente os dados de um aluno e sua referência ao pai."
    )
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable UUID studentId,
            @RequestBody @Valid StudentRequestDTO studentRequestDto) {

        StudentResponseDTO updatedStudent = studentService.updateStudent(studentId, studentRequestDto);
        return ResponseEntity.ok(updatedStudent);
    }

    @Operation(
        operationId = "deleteStudent",
        summary = "Deletar aluno",
        description = "Deleta um aluno com base no ID fornecido."
    )
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId) {

        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        operationId = "archiveStudent",
        summary = "Arquivar aluno",
        description = "Arquiva um aluno com base no ID fornecido."
    )
    @PatchMapping("/{studentId}/archive")
    public ResponseEntity<Void> archiveStudent(@PathVariable UUID studentId) {

        studentService.archiveStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        operationId = "unarchiveStudent",
        summary = "Desarquivar aluno",
        description = "Desarquiva um aluno com base no ID fornecido."
    )
    @PatchMapping("/{studentId}/unarchive")
    public ResponseEntity<Void> unarchiveStudent(@PathVariable UUID studentId) {

        studentService.unarchiveStudent(studentId);
        return ResponseEntity.noContent().build();
    }

}
