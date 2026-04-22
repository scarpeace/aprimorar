package com.aprimorar.api.domain.finance;

import com.aprimorar.api.domain.finance.dto.GeneralExpenseRequestDTO;
import com.aprimorar.api.domain.finance.dto.GeneralExpenseResponseDTO;
import com.aprimorar.api.enums.GeneralExpenseCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/v1/general-expenses")
@Tag(name = "General Expenses", description = "Gerenciamento de despesas gerais")
public class GeneralExpenseController {

    private final GeneralExpenseService service;

    public GeneralExpenseController(GeneralExpenseService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(operationId = "createGeneralExpense", summary = "Cria uma nova despesa geral", description = "Cadastra uma nova despesa administrativa no sistema.")
    @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso.")
    public GeneralExpenseResponseDTO create(@RequestBody @Valid GeneralExpenseRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    @Operation(operationId = "getGeneralExpenses", summary = "Lista todas as despesas gerais com filtros", description = "Retorna uma lista paginada de despesas gerais.")
    @ApiResponse(responseCode = "200", description = "Lista de despesas retornada com sucesso.")
    public Page<GeneralExpenseResponseDTO> findAll(
            @RequestParam(required = false) GeneralExpenseCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(category, startDate, endDate, pageable);
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getGeneralExpenseById", summary = "Busca uma despesa geral pelo ID", description = "Retorna os detalhes de uma despesa específica.")
    @ApiResponse(responseCode = "200", description = "Despesa retornada com sucesso.")
    public GeneralExpenseResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateGeneralExpense", summary = "Atualiza uma despesa geral", description = "Atualiza os dados de uma despesa administrativa existente.")
    @ApiResponse(responseCode = "200", description = "Despesa atualizada com sucesso.")
    public GeneralExpenseResponseDTO update(@PathVariable UUID id, @RequestBody @Valid GeneralExpenseRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(operationId = "deleteGeneralExpense", summary = "Remove uma despesa geral", description = "Deleta permanentemente uma despesa do sistema.")
    @ApiResponse(responseCode = "204", description = "Despesa excluída com sucesso.")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
