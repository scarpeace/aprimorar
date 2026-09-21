package aprimorar.agendamento.colaboradores.web;

import java.util.List;
import java.util.UUID;
import java.net.URI;

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


import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import aprimorar.agendamento.colaboradores.application.ColaboradorService;
import aprimorar.agendamento.colaboradores.web.dto.ColaboradorFiltroRequest;
import aprimorar.agendamento.colaboradores.web.dto.ColaboradorRequest;
import aprimorar.agendamento.colaboradores.web.dto.ColaboradoresOptionsResponse;
import aprimorar.agendamento.colaboradores.web.dto.ColaboradorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instituicao/colaboradores")
@Tag(name = "Colaborador", description = "APIs de gestão de colaboradores")
@CommonProblemResponses
@BadRequestProblemResponse
@ConflictProblemResponse
@NotFoundProblemResponse
class ColaboradorController {

    private final ColaboradorService colaboradorService;

    ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    @Operation(operationId = "criarColaborador", description = "Cria um novo colaborador com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso.")
    public ResponseEntity<Void> createColaborador(
        @RequestBody @Valid ColaboradorRequest colaboradorRequest
    ) {
        UUID id = colaboradorService.createColaborador(colaboradorRequest.toEntity());
        return ResponseEntity.created(URI.create("/instituicao/colaboradores/" + id)).build();
    }

    @GetMapping
    @Operation(operationId = "getColaboradores", description = "Retorna uma lista paginada de colaboradores.")
    @ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso.")
    public ResponseEntity<Page<ColaboradorResponse>> getColaboradores(
        @ParameterObject ColaboradorFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "nome") Pageable pageable
    ) {
        Page<ColaboradorResponse> colaboradores = colaboradorService.getColaboradores(filtro, pageable);
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/options")
    @Operation(operationId = "listColaboradoresOptions", description = "Retorna uma lista de opções de colaboradores para dropdown.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de colaboradores retornada com sucesso.")
    public ResponseEntity<List<ColaboradoresOptionsResponse>> listColaboradoresOptions() {
        List<ColaboradoresOptionsResponse> options = colaboradorService.listColaboradoresOptions();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{colaboradorId}")
    @Operation(operationId = "getColaboradorById", description = "Retorna um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador retornado com sucesso.")
    public ResponseEntity<ColaboradorResponse> getColaboradorById(@PathVariable UUID colaboradorId) {
        ColaboradorResponse colaborador = colaboradorService.findColaboradorById(colaboradorId);
        return ResponseEntity.ok(colaborador);
    }

    @PutMapping("/{colaboradorId}")
    @Operation(operationId = "updateColaborador", description = "Atualiza um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador atualizado com sucesso.")
    public ResponseEntity<Void> updateColaborador(
        @PathVariable UUID colaboradorId,
        @RequestBody @Valid ColaboradorRequest colaboradorRequest
    ) {
        colaboradorService.updateColaborador(colaboradorId, colaboradorRequest.toEntity());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/deactivate")
    @Operation(operationId = "deactivateColaborador", description = "Desativa um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador desativado com sucesso.")
    public ResponseEntity<Void> deactivateColaborador(@PathVariable UUID colaboradorId) {
        colaboradorService.deactivateColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/activate")
    @Operation(operationId = "activateColaborador", description = "Ativa um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador ativado com sucesso.")
    public ResponseEntity<Void> activateColaborador(@PathVariable UUID colaboradorId) {
        colaboradorService.activateColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }
}
