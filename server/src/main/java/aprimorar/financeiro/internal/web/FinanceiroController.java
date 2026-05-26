package aprimorar.financeiro.internal.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import aprimorar.financeiro.api.CategoriaDespesaEnum;
import aprimorar.financeiro.api.FinanceiroQueryApi;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aprimorar.financeiro.api.dto.DespesaRequestDTO;
import aprimorar.financeiro.api.dto.DespesaResponseDTO;
import aprimorar.financeiro.api.dto.DespesasResponseDTO;
import aprimorar.financeiro.internal.application.FinanceiroMutationService;


@RestController
@RequestMapping("/v1/despesas")
@Tag(name = "Financeiro", description = "Gerenciamento de despesas gerais")
public class FinanceiroController {

    private final FinanceiroQueryApi financeiroQueryApi;
    private final FinanceiroMutationService financeiroMutationService;

    public FinanceiroController(FinanceiroQueryApi financeiroQueryApi, FinanceiroMutationService financeiroMutationService) {
        this.financeiroQueryApi = financeiroQueryApi;
        this.financeiroMutationService = financeiroMutationService;
    }

    @PostMapping
    @Operation(operationId = "createDespesa", summary = "Cria uma nova despesa geral")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso.")
    public ResponseEntity<DespesaResponseDTO> createDespesa(@RequestBody @Valid DespesaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeiroMutationService.createDespesa(dto));
    }

    @GetMapping
    @Operation(operationId = "getDespesas", summary = "Lista despesas gerais com filtros")
    @ApiResponse(responseCode = "200", description = "Despesas e resumo financeiro retornados com sucesso.")
    public ResponseEntity<DespesasResponseDTO> getDespesas(
        @RequestParam(required = false) CategoriaDespesaEnum categoria,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(financeiroQueryApi.getDespesas(pageable, categoria, startDate, endDate));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getDespesaById", summary = "Busca uma despesa geral pelo ID")
    @ApiResponse(responseCode = "200", description = "Despesa retornada com sucesso.")
    public ResponseEntity<DespesaResponseDTO> getDespesaById(@PathVariable UUID id) {
        return ResponseEntity.ok(financeiroQueryApi.findDespesaById(id));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateDespesa", summary = "Atualiza uma despesa geral")
    @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso.")
    public ResponseEntity<DespesaResponseDTO> updateDespesa(@PathVariable UUID id, @RequestBody @Valid DespesaRequestDTO dto) {
        return ResponseEntity.ok(financeiroMutationService.updateDespesa(id, dto));
    }

    @PatchMapping("/{id}/toggle-pagamento")
    @Operation(operationId = "toggleDespesaPayment", summary = "Alterna o pagamento de uma despesa geral")
    @ApiResponse(responseCode = "200", description = "Status de pagamento da despesa atualizado com sucesso.")
    public ResponseEntity<DespesaResponseDTO> toggleDespesaPagamento(@PathVariable UUID id) {
        return ResponseEntity.ok(financeiroMutationService.togglePagamento(id));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteDespesa", summary = "Remove uma despesa geral")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso.")
    public ResponseEntity<Void> deleteDespesa(@PathVariable UUID id) {
        financeiroMutationService.deleteDespesa(id);
        return ResponseEntity.noContent().build();
    }
}
