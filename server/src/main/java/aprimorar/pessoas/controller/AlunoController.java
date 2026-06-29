package aprimorar.pessoas.controller;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RestController;

import aprimorar.pessoas.dto.aluno.AlunoFiltroRequest;
import aprimorar.pessoas.dto.aluno.AlunoRequestDTO;
import aprimorar.pessoas.dto.aluno.AlunoResponseDTO;
import aprimorar.pessoas.dto.aluno.AlunosKpisDTO;
import aprimorar.pessoas.dto.aluno.AlunosListDTO;
import aprimorar.pessoas.service.AlunoMutationService;
import aprimorar.pessoas.service.AlunoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/alunos")
@Tag(name = "Aluno", description = "APIs de gestão de alunos")
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
    public ResponseEntity<AlunoResponseDTO> createAluno(@RequestBody @Valid AlunoRequestDTO alunoRequestDTO) {
        AlunoResponseDTO response = alunoMutationService.createAluno(alunoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "getAlunos", description = "Retorna uma lista paginada de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.")
    public ResponseEntity<Page<AlunoResponseDTO>> getAlunos(
        @ParameterObject AlunoFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "nome") Pageable pageable
    ) {
        Page<AlunoResponseDTO> alunos = alunoQueryService.getAlunos(filtro, pageable);
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/kpis")
    @Operation(operationId = "getAlunosKpis", description = "Retorna os KPIs de alunos.")
    @ApiResponse(responseCode = "200", description = "KPIs de alunos retornados com sucesso.")
    public ResponseEntity<AlunosKpisDTO> getAlunosKpis() {
        AlunosKpisDTO kpis = alunoQueryService.getAlunosKpis();
        return ResponseEntity.ok(kpis);
    }

    @GetMapping("/responsavel/{responsavelId}")
    @Operation(operationId = "getAlunosByResponsavel", description = "Retorna uma lista de alunos pelo ID do responsável.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.")

    public ResponseEntity<List<AlunoResponseDTO>> getAlunosPorResponsavel(
        @PathVariable UUID responsavelId
    ) {
        List<AlunoResponseDTO> options = alunoQueryService.getAlunosByResponsavelId(responsavelId);
        return ResponseEntity.ok(options);
    }

    @GetMapping("/options")
    @Operation(operationId = "listAlunos", description = "Retorna uma lista de opções de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de alunos retornada com sucesso.")
    public ResponseEntity<List<AlunosListDTO>> listAlunos() {
        List<AlunosListDTO> options = alunoQueryService.listAlunos();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{alunoId}")
    @Operation(operationId = "getAlunoById", description = "Retorna um aluno por ID.")
    @ApiResponse(responseCode = "200", description = "Aluno retornado com sucesso.")
    public ResponseEntity<AlunoResponseDTO> getAlunoById(@PathVariable UUID alunoId) {
        AlunoResponseDTO foundAluno = alunoQueryService.findAlunoById(alunoId);
        return ResponseEntity.ok(foundAluno);
    }

    @PutMapping("/{alunoId}")
    @Operation(operationId = "updateAluno", description = "Atualiza um aluno por ID.")
    @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso.")
    public ResponseEntity<AlunoResponseDTO> updateAluno(
        @PathVariable UUID alunoId,
        @RequestBody @Valid AlunoRequestDTO dto
    ) {
        AlunoResponseDTO updatedAluno = alunoMutationService.updateAluno(alunoId, dto);
        return ResponseEntity.ok(updatedAluno);
    }

    @DeleteMapping("/{alunoId}")
    @Operation(operationId = "deleteAluno", description = "Deleta um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso.")
    public ResponseEntity<Void> deleteAluno(@PathVariable UUID alunoId) {
        alunoMutationService.deleteAluno(alunoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{alunoId}/archive")
    @Operation(operationId = "archiveAluno", description = "Arquiva um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno arquivado com sucesso.")
    public ResponseEntity<Void> archiveAluno(@PathVariable UUID alunoId) {
        alunoMutationService.archiveAluno(alunoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{alunoId}/unarchive")
    @Operation(operationId = "unarchiveAluno", description = "Desarquiva um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno desarquivado com sucesso.")
    public ResponseEntity<Void> unarchiveAluno(@PathVariable UUID alunoId) {
        alunoMutationService.unarchiveAluno(alunoId);
        return ResponseEntity.noContent().build();
    }
}
