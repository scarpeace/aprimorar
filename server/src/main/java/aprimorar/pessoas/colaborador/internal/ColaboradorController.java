package aprimorar.pessoas.colaborador.internal;

import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorCountSummaryDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorOptionsDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorRequestDTO;
import aprimorar.pessoas.colaborador.api.contract.dto.ColaboradorResponseDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/colaboradores")
@Tag(name = "Colaborador", description = "APIs de gestao de colaboradores")
public class ColaboradorController {

    private final ColaboradorService colaboradorService;

    public ColaboradorController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping
    @Operation(operationId = "criarColaborador", description = "Cria um novo colaborador com os dados fornecidos.")
    @ApiResponse(responseCode = "201", description = "Colaborador criado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> createColaborador(
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDto
    ) {
        ColaboradorResponseDTO response = colaboradorService.createColaborador(colaboradorRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(operationId = "listarColaboradores", description = "Retorna uma lista paginada de colaboradores.")
    @ApiResponse(responseCode = "200", description = "Lista de colaboradores retornada com sucesso.")
    public ResponseEntity<PageDTO<ColaboradorResponseDTO>> listarColaboradores(
        @ParameterObject @PageableDefault(sort = "name") Pageable pageable,
        @RequestParam(required = false) String busca,
        @RequestParam(required = false) Boolean arquivado
    ) {
        PageDTO<ColaboradorResponseDTO> colaboradores = colaboradorService.getColaboradores(pageable, busca, arquivado);
        return ResponseEntity.ok(colaboradores);
    }

    @GetMapping("/summary")
    @Operation(operationId = "obterResumoColaboradores", description = "Retorna indicadores quantitativos dos colaboradores cadastrados.")
    @ApiResponse(responseCode = "200", description = "Resumo de colaboradores retornado com sucesso.")
    public ResponseEntity<ColaboradorCountSummaryDTO> obterResumo() {
        ColaboradorCountSummaryDTO summary = colaboradorService.getSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/options")
    @Operation(operationId = "listarOpcoesColaboradores", description = "Retorna uma lista de opções de colaboradores para dropdown.")
    @ApiResponse(responseCode = "200", description = "Lista de opções de colaboradores retornada com sucesso.")
    public ResponseEntity<List<ColaboradorOptionsDTO>> listarOpcoes() {
        List<ColaboradorOptionsDTO> options = colaboradorService.listarOpcoes();
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{colaboradorId}")
    @Operation(operationId = "buscarColaboradorPorId", description = "Retorna um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador retornado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> buscarPorId(@PathVariable UUID colaboradorId) {
        ColaboradorResponseDTO colaborador = colaboradorService.buscarPorId(colaboradorId);
        return ResponseEntity.ok(colaborador);
    }

    @PatchMapping("/{colaboradorId}")
    @Operation(operationId = "atualizarColaborador", description = "Atualiza um colaborador por ID.")
    @ApiResponse(responseCode = "200", description = "Colaborador atualizado com sucesso.")
    public ResponseEntity<ColaboradorResponseDTO> updateColaborador(
        @PathVariable UUID colaboradorId,
        @RequestBody @Valid ColaboradorRequestDTO colaboradorRequestDTO
    ) {
        ColaboradorResponseDTO colaboradorAtualizado = colaboradorService.updateColaborador(colaboradorId, colaboradorRequestDTO);
        return ResponseEntity.ok(colaboradorAtualizado);
    }

    @DeleteMapping("/{colaboradorId}")
    @Operation(operationId = "deletarColaborador", description = "Deleta um colaborador por ID.")
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
