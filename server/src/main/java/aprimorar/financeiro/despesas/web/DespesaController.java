package aprimorar.financeiro.despesas.web;

import aprimorar.financeiro.despesas.service.DespesaService;
import aprimorar.financeiro.despesas.web.dto.DespesaRequest;
import aprimorar.financeiro.despesas.web.dto.DespesaFiltroRequest;
import aprimorar.financeiro.despesas.web.dto.DespesaResponse;
import aprimorar.common.openapi.BadRequestProblemResponse;
import aprimorar.common.openapi.CommonProblemResponses;
import aprimorar.common.openapi.ConflictProblemResponse;
import aprimorar.common.openapi.NotFoundProblemResponse;
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
@RequestMapping("/despesas")
@Tag(name = "Despesa", description = "APIs de gestão de despesas operacionais")
@CommonProblemResponses
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PostMapping
    @Operation(operationId = "createDespesa", description = "Cria uma nova despesa operacional")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso")
    @BadRequestProblemResponse
    @ConflictProblemResponse
    public ResponseEntity<DespesaResponse> createDespesa(@RequestBody @Valid DespesaRequest despesaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaService.createDespesa(despesaRequest));
    }

    @GetMapping
    @Operation(operationId = "getDespesas", description = "Lista despesas com paginação, ordenação e filtros opcionais")
    @ApiResponse(responseCode = "200", description = "Página de despesas retornada com sucesso")
    @BadRequestProblemResponse
    public ResponseEntity<Page<DespesaResponse>> getDespesas(
        @ParameterObject @Valid DespesaFiltroRequest filtro,
        @ParameterObject @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(despesaService.getDespesas(filtro, pageable));
    }

    @GetMapping("/{despesaId}")
    @Operation(operationId = "getDespesaById", description = "Retorna uma despesa por ID")
    @ApiResponse(responseCode = "200", description = "Despesa retornada com sucesso")
    @NotFoundProblemResponse
    public ResponseEntity<DespesaResponse> getDespesaById(@PathVariable Long despesaId) {
        return ResponseEntity.ok(despesaService.findDespesaById(despesaId));
    }

    @PatchMapping("/{despesaId}")
    @Operation(operationId = "updateDespesa", description = "Atualiza uma despesa por ID")
    @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso")
    @BadRequestProblemResponse
    @NotFoundProblemResponse
    @ConflictProblemResponse
    public ResponseEntity<DespesaResponse> updateDespesa(
        @PathVariable Long despesaId,
        @RequestBody @Valid DespesaRequest despesaRequest
    ) {
        return ResponseEntity.ok(despesaService.updateDespesa(despesaId, despesaRequest));
    }

    @DeleteMapping("/{despesaId}")
    @Operation(operationId = "deleteDespesa", description = "Exclui uma despesa por ID")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso")
    @NotFoundProblemResponse
    public ResponseEntity<Void> deleteDespesa(@PathVariable Long despesaId) {
        despesaService.deleteDespesa(despesaId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{despesaId}/pagar")
    @Operation(operationId = "pagarDespesa", description = "Marca a despesa como paga")
    @ApiResponse(responseCode = "200", description = "Despesa paga com sucesso")
    @NotFoundProblemResponse
    public ResponseEntity<DespesaResponse> pagar(@PathVariable Long despesaId) {
        return ResponseEntity.ok(despesaService.pagar(despesaId));
    }

    @PatchMapping("/{despesaId}/cancelarPagamento")
    @Operation(operationId = "cancelarPagamentoDespesa", description = "Cancela o pagamento da despesa")
    @ApiResponse(responseCode = "200", description = "Pagamento da despesa cancelado com sucesso")
    @NotFoundProblemResponse
    public ResponseEntity<DespesaResponse> cancelarPagamento(@PathVariable Long despesaId) {
        return ResponseEntity.ok(despesaService.cancelarPagamento(despesaId));
    }
}
