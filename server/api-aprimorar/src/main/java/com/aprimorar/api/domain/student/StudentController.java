package com.aprimorar.api.domain.student;

import com.aprimorar.api.domain.student.dto.StudentOptionsDTO;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.shared.PageDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
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

@Slf4j
@RestController
@RequestMapping("/v1/students")
@Tag(name = "Student", description = "Student management APIs")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(operationId = "createStudent", description = "Cria um novo aluno com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso.")
    public ResponseEntity<StudentResponseDTO> createStudent(@RequestBody @Valid StudentRequestDTO createStudentDto) {
        StudentResponseDTO response = studentService.createStudent(createStudentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "getStudents", description = "Retorna uma lista paginada de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.")
    public ResponseEntity<PageDTO<StudentResponseDTO>> getStudents(
        @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean archived
    ) {
        PageDTO<StudentResponseDTO> students = studentService.getStudents(pageable, search, archived);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/parent/{parentId}")
    @Operation(operationId = "getStudentsByParent", description = "Retorna uma lista de alunos pelo ID do pai.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.")
    public ResponseEntity<PageDTO<StudentResponseDTO>> getStudentsByParent(
        @PathVariable UUID parentId,
        @ParameterObject Pageable pageable
    ) {
        PageDTO<StudentResponseDTO> options = studentService.getStudentsByParent(parentId, pageable);
        return ResponseEntity.ok(options);
    }

    @GetMapping("/options")
    @Operation(operationId = "getStudentsOptions", description = "Retorna uma lista de opções de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de alunos retornada com sucesso.")
    public ResponseEntity<List<StudentOptionsDTO>> getStudentOptions() {
        List<StudentOptionsDTO> options = studentService.getStudentOptions();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{studentId}")
    @Operation(operationId = "getStudentById", description = "Retorna um aluno por ID.")
    @ApiResponse(responseCode = "200", description = "Aluno retornado com sucesso.")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable UUID studentId) {
        StudentResponseDTO foundStudent = studentService.findById(studentId);
        return ResponseEntity.ok(foundStudent);
    }

    @PutMapping("/{studentId}")
    @Operation(operationId = "updateStudent", description = "Atualiza um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno atualizado com sucesso.")
    public ResponseEntity<Void> updateStudent(
        @PathVariable UUID studentId,
        @RequestBody @Valid StudentRequestDTO dto
    ) {
        studentService.updateStudent(dto, studentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studentId}")
    @Operation(operationId = "deleteStudent", description = "Deleta um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso.")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{studentId}/archive")
    @Operation(operationId = "archiveStudent", description = "Arquiva um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno arquivado com sucesso.")
    public ResponseEntity<Void> archiveStudent(@PathVariable UUID studentId) {
        studentService.archiveStudent(studentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{studentId}/unarchive")
    @Operation(operationId = "unarchiveStudent", description = "Desarquiva um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno desarquivado com sucesso.")
    public ResponseEntity<Void> unarchiveStudent(@PathVariable UUID studentId) {
        studentService.unarchiveStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}
