package com.aprimorar.api.domain.finance.internal;

import com.aprimorar.api.domain.employee.api.dto.EmployeeSummaryDTO;
import com.aprimorar.api.domain.finance.api.FinanceService;
import com.aprimorar.api.domain.finance.api.dto.FinanceSummaryDTO;
import com.aprimorar.api.domain.finance.api.dto.TransactionRequestDTO;
import com.aprimorar.api.domain.finance.api.dto.TransactionResponseDTO;
import com.aprimorar.api.domain.student.api.dto.StudentSummaryDTO;
import com.aprimorar.api.enums.TransactionCategory;
import com.aprimorar.api.enums.TransactionStatus;
import com.aprimorar.api.enums.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/finance")
@Tag(name = "Finance", description = "Gerenciamento financeiro e de transações")
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

    @GetMapping("/students/{id}/summary")
    @Operation(operationId = "getFinanceStudentSummary", summary = "Resumo financeiro do aluno")
    public ResponseEntity<StudentSummaryDTO> getStudentSummary(
        @PathVariable UUID id,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate
    ) {
        return ResponseEntity.ok(financeService.getStudentSummary(id, startDate, endDate));
    }

    @GetMapping("/employees/{id}/summary")
    @Operation(operationId = "getFinanceEmployeeSummary", summary = "Resumo financeiro do colaborador")
    public ResponseEntity<EmployeeSummaryDTO> getEmployeeSummary(
        @PathVariable UUID id,
        @RequestParam(required = false) Instant startDate,
        @RequestParam(required = false) Instant endDate
    ) {
        return ResponseEntity.ok(financeService.getEmployeeSummary(id, startDate, endDate));
    }

    @PostMapping("/general-expenses")
    @Operation(operationId = "createGeneralExpense", summary = "Cria uma nova despesa geral", description = "Cadastra uma nova despesa administrativa no sistema.")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso.")
    public ResponseEntity<TransactionResponseDTO> createGeneralExpense(@RequestBody @Valid TransactionRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(financeService.createGeneralExpense(dto));
    }

    @GetMapping("/general-expenses")
    @Operation(operationId = "getGeneralExpenses", summary = "Lista todas as despesas gerais com filtros", description = "Retorna uma lista paginada de despesas gerais.")
    @ApiResponse(responseCode = "200", description = "Lista de despesas retornada com sucesso.")
    public ResponseEntity<Page<TransactionResponseDTO>> getGeneralExpenses(
        @RequestParam(required = false) TransactionCategory category,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(financeService.findAllGeneralExpenses(category, startDate, endDate, pageable));
    }

    @GetMapping("/general-expenses/{id}")
    @Operation(operationId = "getGeneralExpenseById", summary = "Busca uma despesa geral pelo ID", description = "Retorna os detalhes de uma despesa específica.")
    @ApiResponse(responseCode = "200", description = "Despesa retornada com sucesso.")
    public ResponseEntity<TransactionResponseDTO> getGeneralExpenseById(@PathVariable UUID id) {
        return ResponseEntity.ok(financeService.findGeneralExpenseById(id));
    }

    @PutMapping("/general-expenses/{id}")
    @Operation(operationId = "updateGeneralExpense", summary = "Atualiza uma despesa geral", description = "Atualiza os dados de uma despesa administrativa existente.")
    @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso.")
    public ResponseEntity<TransactionResponseDTO> updateGeneralExpense(@PathVariable UUID id, @RequestBody @Valid TransactionRequestDTO dto) {
        return ResponseEntity.ok(financeService.updateGeneralExpense(id, dto));
    }

    @DeleteMapping("/general-expenses/{id}")
    @Operation(operationId = "deleteGeneralExpense", summary = "Remove uma despesa geral", description = "Deleta permanentemente uma despesa do sistema.")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso.")
    public ResponseEntity<Void> deleteGeneralExpense(@PathVariable UUID id) {
        financeService.deleteGeneralExpense(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/transactions")
    @Operation(operationId = "getFinanceTransactions", summary = "Lista transações financeiras")
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactions(
        @RequestParam(required = false) TransactionCategory category,
        @RequestParam(required = false) TransactionType type,
        @RequestParam(required = false) TransactionStatus status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(financeService.findTransactions(category, type, status, startDate, endDate, pageable));
    }
}
