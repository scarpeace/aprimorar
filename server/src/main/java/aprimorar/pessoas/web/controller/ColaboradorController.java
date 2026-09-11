package aprimorar.pessoas.web.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.pessoas.service.ColaboradorServiceImpl;
import aprimorar.pessoas.web.dto.colaborador.ColaboradorFiltroRequest;
import aprimorar.pessoas.web.dto.colaborador.ColaboradorDetailResponseDTO;
import aprimorar.pessoas.web.dto.colaborador.ColaboradorListResponseDTO;
import aprimorar.pessoas.web.dto.colaborador.ColaboradorRequestDTO;
import aprimorar.pessoas.web.dto.colaborador.ColaboradoresOptionsDTO;
import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/colaboradores")
@Tag(name = "Colaborador", description = "APIs de gestão de colaboradores")
@CommonProblemResponses
public class ColaboradorController {

    private final ColaboradorServiceImpl colaboradorService;

    public ColaboradorController(ColaboradorServiceImpl colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    @Operation(operationId = "createColaborador", description = "Cria um novo colaborador com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso.")
    @BadRequestProblemResponse
    @ConflictProblemResponse
    public ResponseEntity<Void> createColaborador(
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDto
    ) {
        UUID id = colaboradorService.createColaborador(colaboradorRequestDto);
        return ResponseEntity.created(URI.create("/v1/colaboradores/" + id)).build();
    }

    @GetMapping
    @Operation(operationId = "getColaboradores", description = "Retorna uma lista paginada de colaboradores.")
    @ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso.")
    @BadRequestProblemResponse
    public ResponseEntity<Page<ColaboradorListResponseDTO>> getColaboradores(
        @ParameterObject ColaboradorFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "nome") Pageable pageable
    ) {
        Page<ColaboradorListResponseDTO> colaboradores = colaboradorService.getColaboradores(filtro, pageable);
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/list")
    @Operation(operationId = "getColaboradoresList", description = "Retorna uma lista de opções de colaboradores para dropdown.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de colaboradores retornada com sucesso.")
    public ResponseEntity<List<ColaboradoresOptionsDTO>> listarColaboradores() {
        List<ColaboradoresOptionsDTO> options = colaboradorService.getColaboradoresOptions();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{colaboradorId}")
    @Operation(operationId = "findColaboradorById", description = "Retorna um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador retornado com sucesso.")
    @NotFoundProblemResponse
    public ResponseEntity<ColaboradorDetailResponseDTO> buscarPorId(@PathVariable UUID colaboradorId) {
        ColaboradorDetailResponseDTO colaborador = colaboradorService.findById(colaboradorId);
        return ResponseEntity.ok(colaborador);
    }

    @PatchMapping("/{colaboradorId}")
    @Operation(operationId = "updateColaborador", description = "Atualiza um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso.")
    @BadRequestProblemResponse
    @ConflictProblemResponse
    @NotFoundProblemResponse
    public ResponseEntity<Void> updateColaborador(
        @PathVariable UUID colaboradorId,
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDTO
    ) {
        colaboradorService.updateColaborador(colaboradorId, colaboradorRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/deactivate")
    @Operation(operationId = "deactivateColaborador", description = "Desativa um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador desativado com sucesso.")
    @NotFoundProblemResponse
    public ResponseEntity<Void> deactivateColaborador(@PathVariable UUID colaboradorId) {
        colaboradorService.deactivateColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/activate")
    @Operation(operationId = "activateColaborador", description = "Ativa um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador ativado com sucesso.")
    @NotFoundProblemResponse
    public ResponseEntity<Void> activateColaborador(@PathVariable UUID colaboradorId) {
        colaboradorService.activateColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }
}
