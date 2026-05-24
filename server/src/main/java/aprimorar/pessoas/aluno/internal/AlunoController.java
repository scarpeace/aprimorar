package aprimorar.pessoas.aluno.internal;

import aprimorar.pessoas.aluno.api.dto.AlunoOptionsDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoRequestDTO;
import aprimorar.pessoas.aluno.api.dto.AlunoResponseDTO;
import aprimorar.shared.PageDTO;
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
@RequestMapping("/v1/alunos")
@Tag(name = "Aluno", description = "APIs de gestao de alunos")
public class AlunoController {

    private final AlunoMutationService alunoMutationService;
    private final AlunoQueryService alunoQueryService;

    public AlunoController(AlunoMutationService alunoMutationService, AlunoQueryService alunoQueryService) {
        this.alunoMutationService = alunoMutationService;
        this.alunoQueryService = alunoQueryService;
    }

    @PostMapping
    @Operation(operationId = "criarAluno", description = "Cria um novo aluno com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso.")
    public ResponseEntity<AlunoResponseDTO> createStudent(@RequestBody @Valid AlunoRequestDTO createStudentDto) {
        AlunoResponseDTO response = alunoMutationService.createAluno(createStudentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "listarAlunos", description = "Retorna uma lista paginada de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.")
    public ResponseEntity<PageDTO<AlunoResponseDTO>> getStudents(
        @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
        @RequestParam(required = false) String search,
        @RequestParam(required = false) Boolean archived
    ) {
        PageDTO<AlunoResponseDTO> students = alunoQueryService.getAlunos(pageable, search, archived);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/responsavel/{parentId}")
    @Operation(operationId = "listarAlunosPorResponsavel", description = "Retorna uma lista de alunos pelo ID do responsavel.")
   @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.")
   public ResponseEntity<List<AlunoResponseDTO>> getAlunosPorResponsavel(
       @PathVariable UUID parentId
   ) {
       List<AlunoResponseDTO> options = alunoQueryService.getAlunosPorResponsavel(parentId);
       return ResponseEntity.ok(options);
   }

    @GetMapping("/options")
    @Operation(operationId = "listarOpcoesAlunos", description = "Retorna uma lista de opcoes de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de alunos retornada com sucesso.")
    public ResponseEntity<List<AlunoOptionsDTO>> listAlunos() {
        List<AlunoOptionsDTO> options = alunoQueryService.listAlunos();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{studentId}")
    @Operation(operationId = "buscarAlunoPorId", description = "Retorna um aluno por ID.")
    @ApiResponse(responseCode = "200", description = "Aluno retornado com sucesso.")
    public ResponseEntity<AlunoResponseDTO> getStudentById(@PathVariable UUID studentId) {
        AlunoResponseDTO foundAluno = alunoQueryService.findAlunoById(studentId);
        return ResponseEntity.ok(foundAluno);
    }

    @PutMapping("/{studentId}")
    @Operation(operationId = "atualizarAluno", description = "Atualiza um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno atualizado com sucesso.")
    public ResponseEntity<Void> updateStudent(
        @PathVariable UUID studentId,
        @RequestBody @Valid AlunoRequestDTO dto
    ) {
        alunoMutationService.updateAluno(studentId, dto );
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{studentId}")
    @Operation(operationId = "deletarAluno", description = "Deleta um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso.")
    public ResponseEntity<Void> deleteStudent(@PathVariable UUID studentId) {
        alunoMutationService.deleteAluno(studentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{studentId}/archive")
    @Operation(operationId = "arquivarAluno", description = "Arquiva um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno arquivado com sucesso.")
    public ResponseEntity<Void> archiveStudent(@PathVariable UUID studentId) {
        alunoMutationService.archiveAluno(studentId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{studentId}/unarchive")
    @Operation(operationId = "desarquivarAluno", description = "Desarquiva um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno desarquivado com sucesso.")
    public ResponseEntity<Void> unarchiveStudent(@PathVariable UUID studentId) {
        alunoMutationService.unarchiveAluno(studentId);
        return ResponseEntity.noContent().build();
    }
}
