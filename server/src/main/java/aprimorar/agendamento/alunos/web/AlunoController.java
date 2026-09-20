package aprimorar.agendamento.alunos.web;

import java.util.List;
import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.agendamento.alunos.web.dto.aluno.AlunoListResponse;
import aprimorar.agendamento.alunos.web.dto.aluno.AlunosOptionsResponse;
import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.agendamento.alunos.service.AlunoService;
import aprimorar.agendamento.alunos.web.dto.aluno.AlunoResponse;
import aprimorar.agendamento.alunos.web.dto.aluno.AlunoFiltroRequest;
import aprimorar.agendamento.alunos.web.dto.aluno.AlunoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instituicao/alunos")
@Tag(name = "Aluno", description = "APIs de gestão de alunos")
@CommonProblemResponses
@BadRequestProblemResponse
@ConflictProblemResponse
@NotFoundProblemResponse
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    @Operation(operationId = "criarAluno", description = "Cria um novo aluno com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso.")
    public ResponseEntity<Void> createAluno(@RequestBody @Valid AlunoRequest alunoRequest) {
        UUID id = alunoService.createAluno(alunoRequest.toEntity());
        return ResponseEntity.created(URI.create("/instituicao/alunos/" + id)).build();
    }

    @GetMapping
    @Operation(operationId = "getAlunos", description = "Retorna uma lista paginada de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso.")
    public ResponseEntity<Page<AlunoListResponse>> getAlunos(
        @ParameterObject AlunoFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "nome") Pageable pageable
    ) {
        Page<AlunoListResponse> alunos = alunoService.getAlunos(filtro, pageable)
            .map(AlunoListResponse::toDto);
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/options")
    @Operation(operationId = "listAlunosOptions", description = "Retorna uma lista de opções de alunos.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de alunos retornada com sucesso.")
    public ResponseEntity<List<AlunosOptionsResponse>> listAlunosOptions() {
        List<AlunosOptionsResponse> options = alunoService.listAlunosOptions()
            .stream()
            .map(AlunosOptionsResponse::toDto)
            .toList();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{alunoId}")
    @Operation(operationId = "getAlunoById", description = "Retorna um aluno por ID.")
    @ApiResponse(responseCode = "200", description = "Aluno retornado com sucesso.")
    public ResponseEntity<AlunoResponse> getAlunoById(@PathVariable UUID alunoId) {
        AlunoResponse foundAluno = AlunoResponse.toDto(alunoService.findAlunoById(alunoId));
        return ResponseEntity.ok(foundAluno);
    }

    @PutMapping("/{alunoId}")
    @Operation(operationId = "updateAluno", description = "Atualiza um aluno por ID.")
    @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso.")
    public ResponseEntity<Void> updateAluno(
        @PathVariable UUID alunoId,
        @RequestBody @Valid AlunoRequest request
    ) {
        alunoService.updateAluno(alunoId, request.toEntity());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{alunoId}/deactivate")
    @Operation(operationId = "deactivateAluno", description = "Desativa um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno desativado com sucesso.")
    public ResponseEntity<Void> deactivateAluno(@PathVariable UUID alunoId) {
        alunoService.deactivateAluno(alunoId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{alunoId}/activate")
    @Operation(operationId = "activateAluno", description = "Ativa um aluno por ID.")
    @ApiResponse(responseCode = "204", description = "Aluno ativado com sucesso.")
    public ResponseEntity<Void> activateAluno(@PathVariable UUID alunoId) {
        alunoService.activateAluno(alunoId);
        return ResponseEntity.noContent().build();
    }
}
