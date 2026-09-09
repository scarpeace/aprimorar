package aprimorar.pessoas.colaborador.web.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.pessoas.colaborador.ColaboradorServiceImpl;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorFiltroRequest;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.web.dto.ColaboradoresOptionsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/colaboradores")
@Tag(name = "Colaborador", description = "APIs de gestão de colaboradores")
public class ColaboradorController {

    private final ColaboradorServiceImpl colaboradorService;

    public ColaboradorController(ColaboradorServiceImpl colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    @Operation(operationId = "createColaborador", description = "Cria um novo colaborador com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> createColaborador(
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDto
    ) {
        ColaboradorResponseDTO response = colaboradorService.createColaborador(colaboradorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "getColaboradores", description = "Retorna uma lista paginada de colaboradores.")
    @ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso.")
    public ResponseEntity<Page<ColaboradorResponseDTO>> getColaboradores(
        @ParameterObject ColaboradorFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "nome") Pageable pageable
    ) {
        Page<ColaboradorResponseDTO> colaboradores = colaboradorService.getColaboradores(filtro, pageable);
        return ResponseEntity.ok(colaboradores);
    }

    // @GetMapping("/kpis")
    // @Operation(operationId = "getColaboradoresKpis", description = "Retorna os KPIs de colaboradores.")
    // @ApiResponse(responseCode = "200", description = "KPIs de colaboradores retornados com sucesso.")
    // public ResponseEntity<ColaboradoresKpisDTO> getColaboradoresKpis() {
    //     ColaboradoresKpisDTO kpis = colaboradorQueryService.getColaboradoresKpis();
    //     return ResponseEntity.ok(kpis);
    // }

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
    public ResponseEntity<ColaboradorResponseDTO> buscarPorId(@PathVariable UUID colaboradorId) {
        ColaboradorResponseDTO colaborador = colaboradorService.findById(colaboradorId);
        return ResponseEntity.ok(colaborador);
    }

    @PatchMapping("/{colaboradorId}")
    @Operation(operationId = "updateColaborador", description = "Atualiza um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> updateColaborador(
        @PathVariable UUID colaboradorId,
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDTO
    ) {
        ColaboradorResponseDTO colaboradorAtualizado = colaboradorService.updateColaborador(colaboradorId, colaboradorRequestDTO);
        return ResponseEntity.ok(colaboradorAtualizado);
    }

    @DeleteMapping("/{colaboradorId}")
    @Operation(operationId = "deleteColaborador", description = "Deleta um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador deletado com sucesso.")
    public ResponseEntity<Void> deleteColaborador(@PathVariable UUID colaboradorId) {
        colaboradorService.deleteColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/archive")
    @Operation(operationId = "arquivarColaborador", description = "Arquiva um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador arquivado com sucesso.")
    public ResponseEntity<Void> archiveColaborador(@PathVariable UUID colaboradorId) {
        colaboradorService.archiveColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/unarchive")
    @Operation(operationId = "desarquivarColaborador", description = "Desarquiva um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador desarquivado com sucesso.")
    public ResponseEntity<Void> unarchiveColaborador(@PathVariable UUID colaboradorId) {
        colaboradorService.unarchiveColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }
}
