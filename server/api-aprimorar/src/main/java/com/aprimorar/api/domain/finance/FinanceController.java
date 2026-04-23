package com.aprimorar.api.domain.finance;

import com.aprimorar.api.domain.finance.dto.FinanceSummaryDTO;
import com.aprimorar.api.domain.finance.dto.GeneralExpenseRequestDTO;
import com.aprimorar.api.domain.finance.dto.GeneralExpenseResponseDTO;
import com.aprimorar.api.enums.GeneralExpenseCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/v1/finance")
@Tag(name = "Finance", description = "Gerenciamento financeiro e de despesas gerais")
public class FinanceController {

    private final FinanceService financeService;

    public FinanceController(FinanceService financeService) {
        this.financeService = financeService;
    }

    @GetMapping("/summary")
    @Operation(operationId = "getFinanceSummary", summary = "Resumo financeiro", description = "Retorna o resumo financeiro consolidado.")
    @ApiResponse(responseCode = "200", description = "Resumo financeiro retornado com sucesso.")
    public ResponseEntity<FinanceSummaryDTO> getFinanceSummary() {
        return ResponseEntity.ok(financeService.getFinanceSummary());
    }

    @PostMapping("/general-expenses")
    @Operation(operationId = "createGeneralExpense", summary = "Cria uma nova despesa geral", description = "Cadastra uma nova despesa administrativa no sistema.")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso.")
    public ResponseEntity<GeneralExpenseResponseDTO> createGeneralExpense(@RequestBody @Valid GeneralExpenseRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.createGeneralExpense(dto));
    }

    @GetMapping("/general-expenses")
    @Operation(operationId = "getGeneralExpenses", summary = "Lista todas as despesas gerais com filtros", description = "Retorna uma lista paginada de despesas gerais.")
    @ApiResponse(responseCode = "200", description = "Lista de despesas retornada com sucesso.")
    public ResponseEntity<Page<GeneralExpenseResponseDTO>> getGeneralExpenses(
            @RequestParam(required = false) GeneralExpenseCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(financeService.findAllGeneralExpenses(category, startDate, endDate, pageable));
    }

    @GetMapping("/general-expenses/{id}")
    @Operation(operationId = "getGeneralExpenseById", summary = "Busca uma despesa geral pelo ID", description = "Retorna os detalhes de uma despesa específica.")
    @ApiResponse(responseCode = "200", description = "Despesa retornada com sucesso.")
    public ResponseEntity<GeneralExpenseResponseDTO> getGeneralExpenseById(@PathVariable UUID id) {
        return ResponseEntity.ok(financeService.findGeneralExpenseById(id));
    }

    @PutMapping("/general-expenses/{id}")
    @Operation(operationId = "updateGeneralExpense", summary = "Atualiza uma despesa geral", description = "Atualiza os dados de uma despesa administrativa existente.")
    @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso.")
    public ResponseEntity<GeneralExpenseResponseDTO> updateGeneralExpense(@PathVariable UUID id, @RequestBody @Valid GeneralExpenseRequestDTO dto) {
        return ResponseEntity.ok(financeService.updateGeneralExpense(id, dto));
    }

    @DeleteMapping("/general-expenses/{id}")
    @Operation(operationId = "deleteGeneralExpense", summary = "Remove uma despesa geral", description = "Deleta permanentemente uma despesa do sistema.")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso.")
    public ResponseEntity<Void> deleteGeneralExpense(@PathVariable UUID id) {
        financeService.deleteGeneralExpense(id);
        return ResponseEntity.noContent().build();
    }
}
