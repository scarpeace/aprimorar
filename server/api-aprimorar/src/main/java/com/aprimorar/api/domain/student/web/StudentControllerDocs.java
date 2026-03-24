package com.aprimorar.api.domain.student.web;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.aprimorar.api.domain.student.dto.StudentOptionDTO;
import com.aprimorar.api.domain.student.dto.StudentRequestDTO;
import com.aprimorar.api.domain.student.dto.StudentResponseDTO;
import com.aprimorar.api.exception.ProblemDetailResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Student", description = "Student management APIs")
public interface StudentControllerDocs {

    @Operation(
            operationId = "createStudent",
            summary = "Criar aluno",
            description = "Cria um novo aluno com os dados fornecidos."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Aluno criado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StudentResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<StudentResponseDTO> createStudent(StudentRequestDTO request);

    @Operation(
            operationId = "getStudents",
            summary = "Listar alunos",
            description = "Retorna todos os alunos do banco de dados com paginação."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem dos alunos com paginação"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Page<StudentResponseDTO>> getStudents(
            @Parameter(description = "Página com informações de paginação") Pageable pageable,
            @Parameter(description = "Termo de busca") String search
    );

    @Operation(
            operationId = "getStudentOptions",
            summary = "Listar alunos para opções e dropdown",
            description = "Lista todos os alunos para uso em opções e dropdowns."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem dos alunos para opções e dropdown",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = StudentOptionDTO.class)))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<List<StudentOptionDTO>> getStudentOptions();

    @Operation(
            operationId = "getStudentsByParent",
            summary = "Listar alunos por pai",
            description = "Retorna todos os alunos do banco de dados com paginação."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem dos alunos por pai",
                content = @Content(mediaType = "application/json",
                        array = @ArraySchema(schema = @Schema(implementation = StudentResponseDTO.class)))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<List<StudentResponseDTO>> getStudentsByParent(
            @Parameter(description = "ID do pai") UUID parentId
    );

    @Operation(
            operationId = "getStudentById",
            summary = "Listar aluno por ID",
            description = "Retorna um aluno específico com base no ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listagem do aluno por ID",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StudentResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<StudentResponseDTO> getStudentById(UUID studentId);

    @Operation(
            operationId = "updateStudent",
            summary = "Atualizar aluno",
            description = "Atualiza totalmente os dados de um aluno e sua referência ao pai."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = StudentResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<StudentResponseDTO> updateStudent(
            @Parameter(description = "ID do aluno") UUID studentId,
            @Parameter(description = "Dados do aluno") StudentRequestDTO studentRequestDto
    );

    @Operation(
            operationId = "deleteStudent",
            summary = "Deletar aluno",
            description = "Deleta um aluno com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> deleteStudent(UUID studentId);

    @Operation(
            operationId = "archiveStudent",
            summary = "Arquivar aluno",
            description = "Arquiva um aluno com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Aluno arquivado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> archiveStudent(UUID studentId);

    @Operation(
            operationId = "unarchiveStudent",
            summary = "Desarquivar aluno",
            description = "Desarquiva um aluno com base no ID fornecido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Aluno desarquivado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = ProblemDetailResponseDTO.class)))
    })
    ResponseEntity<Void> unarchiveStudent(UUID studentId);

}
