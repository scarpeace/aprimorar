package aprimorar.pessoas.colaborador.internal.web;

import java.util.List;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.pessoas.colaborador.api.ColaboradorQueryApi;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresListDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradoresResponseDTO;
import aprimorar.pessoas.colaborador.api.dto.ColaboradorResponseDTO;
import aprimorar.pessoas.colaborador.internal.application.ColaboradorMutationService;
import aprimorar.pessoas.colaborador.internal.web.dto.ColaboradorRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/colaboradores")
@Tag(name = "Colaborador", description = "APIs de gestão de colaboradores")
public class ColaboradorController {

    private final ColaboradorQueryApi colaboradorQueryService;
    private final ColaboradorMutationService colaboradorMutationService;

    public ColaboradorController(
        ColaboradorQueryApi colaboradorQueryService,
        ColaboradorMutationService colaboradorMutationService
    ) {
        this.colaboradorQueryService = colaboradorQueryService;
        this.colaboradorMutationService = colaboradorMutationService;
    }

    @PostMapping
    @Operation(operationId = "createColaborador", description = "Cria um novo colaborador com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> createColaborador(
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDto
    ) {
        ColaboradorResponseDTO response = colaboradorMutationService.createColaborador(colaboradorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "getColaboradores", description = "Retorna uma lista paginada de colaboradores.")
    @ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso.")
    public ResponseEntity<ColaboradoresResponseDTO> getColaboradores(
        @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
        @RequestParam(required = false) String busca,
        @RequestParam(required = false) Boolean arquivado
    ) {
        ColaboradoresResponseDTO colaboradores = colaboradorQueryService.getColaboradores(pageable, busca, arquivado);
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/options")
    @Operation(operationId = "listColaboradores", description = "Retorna uma lista de opções de colaboradores para dropdown.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de colaboradores retornada com sucesso.")
    public ResponseEntity<List<ColaboradoresListDTO>> listarOpcoes() {
        List<ColaboradoresListDTO> options = colaboradorQueryService.listColaboradores();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{colaboradorId}")
    @Operation(operationId = "findColaboradorById", description = "Retorna um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador retornado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> buscarPorId(@PathVariable UUID colaboradorId) {
        ColaboradorResponseDTO colaborador = colaboradorQueryService.findColaboradorById(colaboradorId);
        return ResponseEntity.ok(colaborador);
    }

    @PatchMapping("/{colaboradorId}")
    @Operation(operationId = "updateColaborador", description = "Atualiza um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> updateColaborador(
        @PathVariable UUID colaboradorId,
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDTO
    ) {
        ColaboradorResponseDTO colaboradorAtualizado = colaboradorMutationService.updateColaborador(colaboradorId, colaboradorRequestDTO);
        return ResponseEntity.ok(colaboradorAtualizado);
    }

    @DeleteMapping("/{colaboradorId}")
    @Operation(operationId = "deleteColaborador", description = "Deleta um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador deletado com sucesso.")
    public ResponseEntity<Void> deleteColaborador(@PathVariable UUID colaboradorId) {
        colaboradorMutationService.deleteColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/archive")
    @Operation(operationId = "arquivarColaborador", description = "Arquiva um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador arquivado com sucesso.")
    public ResponseEntity<Void> archiveColaborador(@PathVariable UUID colaboradorId) {
        colaboradorMutationService.archiveColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{colaboradorId}/unarchive")
    @Operation(operationId = "desarquivarColaborador", description = "Desarquiva um colaborador por ID.")
    @ApiResponse(responseCode = "204", description = "Colaborador desarquivado com sucesso.")
    public ResponseEntity<Void> unarchiveColaborador(@PathVariable UUID colaboradorId) {
        colaboradorMutationService.unarchiveColaborador(colaboradorId);
        return ResponseEntity.noContent().build();
    }
}
