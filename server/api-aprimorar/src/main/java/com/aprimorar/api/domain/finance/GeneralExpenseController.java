package com.aprimorar.api.domain.finance;

import com.aprimorar.api.domain.finance.dto.GeneralExpenseRequestDTO;
import com.aprimorar.api.domain.finance.dto.GeneralExpenseResponseDTO;
import com.aprimorar.api.enums.GeneralExpenseCategory;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Cria uma nova despesa geral")
    public GeneralExpenseResponseDTO create(@RequestBody @Valid GeneralExpenseRequestDTO dto) {
        return service.create(dto);
    }

    @GetMapping
    @Operation(summary = "Lista todas as despesas gerais com filtros")
    public Page<GeneralExpenseResponseDTO> findAll(
            @RequestParam(required = false) GeneralExpenseCategory category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20) Pageable pageable) {
        return service.findAll(category, startDate, endDate, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma despesa geral pelo ID")
    public GeneralExpenseResponseDTO findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma despesa geral")
    public GeneralExpenseResponseDTO update(@PathVariable UUID id, @RequestBody @Valid GeneralExpenseRequestDTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove uma despesa geral")
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
