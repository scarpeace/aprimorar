package aprimorar.expense.internal;

import aprimorar.expense.api.ExpenseCategory;
import aprimorar.expense.api.dto.ExpenseRequestDTO;
import aprimorar.expense.api.dto.ExpenseResponseDTO;
import aprimorar.expense.api.dto.ExpensesSummaryDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/expenses")
@Tag(name = "Expense", description = "Gerenciamento de despesas gerais")
public class ExpenseController {

    private final ExpenseServiceImpl expenseService;

    public ExpenseController(ExpenseServiceImpl expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @Operation(operationId = "createExpense", summary = "Cria uma nova despesa geral")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso.")
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody @Valid ExpenseRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(expenseService.createExpense(dto));
    }

    @GetMapping
    @Operation(operationId = "getExpenses", summary = "Lista despesas gerais com filtros")
    @ApiResponse(responseCode = "200", description = "Despesas e resumo financeiro retornados com sucesso.")
    public ResponseEntity<ExpensesSummaryDTO> getExpenses(
        @RequestParam(required = false) ExpenseCategory category,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @ParameterObject @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(expenseService.getExpenses(category, startDate, endDate, pageable));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getExpenseById", summary = "Busca uma despesa geral pelo ID")
    @ApiResponse(responseCode = "200", description = "Despesa retornada com sucesso.")
    public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable UUID id) {
        return ResponseEntity.ok(expenseService.findExpenseById(id));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateExpense", summary = "Atualiza uma despesa geral")
    @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso.")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(@PathVariable UUID id, @RequestBody @Valid ExpenseRequestDTO dto) {
        return ResponseEntity.ok(expenseService.updateExpense(id, dto));
    }

    @PatchMapping("/{id}/toggle-payment")
    @Operation(operationId = "toggleExpensePayment", summary = "Alterna o pagamento de uma despesa geral")
    @ApiResponse(responseCode = "200", description = "Status de pagamento da despesa atualizado com sucesso.")
    public ResponseEntity<ExpenseResponseDTO> togglePayment(@PathVariable UUID id) {
        return ResponseEntity.ok(expenseService.togglePayment(id));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteExpense", summary = "Remove uma despesa geral")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExpense(@PathVariable UUID id) {
        expenseService.deleteExpense(id);
    }
}
