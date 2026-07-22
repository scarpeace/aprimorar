package aprimorar.despesas.controller;

import aprimorar.despesas.dto.DespesaFiltroRequest;
import aprimorar.despesas.dto.DespesaRequest;
import aprimorar.despesas.dto.DespesaResponse;
import aprimorar.despesas.service.DespesaMutationService;
import aprimorar.despesas.service.DespesaQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

@RestController
@RequestMapping("/v1/despesas")
@Tag(name = "Despesa", description = "APIs de gestão de despesas operacionais")
public class DespesaController {

    private final DespesaQueryService despesaQueryService;
    private final DespesaMutationService despesaMutationService;

    public DespesaController(DespesaQueryService despesaQueryService, DespesaMutationService despesaMutationService) {
        this.despesaQueryService = despesaQueryService;
        this.despesaMutationService = despesaMutationService;
    }

    @PostMapping
    @Operation(operationId = "createDespesa", description = "Cria uma nova despesa operacional")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso")
    public ResponseEntity<DespesaResponse> createDespesa(@RequestBody @Valid DespesaRequest despesaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaMutationService.createDespesa(despesaRequest));
    }

    @GetMapping
    @Operation(operationId = "getDespesas", description = "Lista despesas com paginação, ordenação e filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Página de despesas retornada com sucesso")
    public ResponseEntity<Page<DespesaResponse>> getDespesas(
        @ParameterObject @Valid DespesaFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "dataPagamento", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(despesaQueryService.getDespesas(filtro, pageable));
    }

    @GetMapping("/{despesaId}")
    @Operation(operationId = "getDespesaById", description = "Retorna uma despesa por ID")
    @ApiResponse(responseCode = "200", description = "Despesa retornada com sucesso")
    public ResponseEntity<DespesaResponse> getDespesaById(@PathVariable Long despesaId) {
        return ResponseEntity.ok(despesaQueryService.findDespesaById(despesaId));
    }

    @PatchMapping("/{despesaId}")
    @Operation(operationId = "updateDespesa", description = "Atualiza uma despesa por ID")
    @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso")
    public ResponseEntity<DespesaResponse> updateDespesa(
        @PathVariable Long despesaId,
        @RequestBody @Valid DespesaRequest despesaRequest
    ) {
        return ResponseEntity.ok(despesaMutationService.updateDespesa(despesaId, despesaRequest));
    }

    @DeleteMapping("/{despesaId}")
    @Operation(operationId = "deleteDespesa", description = "Exclui uma despesa por ID")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso")
    public ResponseEntity<Void> deleteDespesa(@PathVariable Long despesaId) {
        despesaMutationService.deleteDespesa(despesaId);
        return ResponseEntity.noContent().build();
    }
}
