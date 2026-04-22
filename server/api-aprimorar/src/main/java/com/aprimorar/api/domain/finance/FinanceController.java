package com.aprimorar.api.domain.finance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.finance.dto.FinanceSummaryDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/finance")
@RequiredArgsConstructor
@Tag(name = "Finance", description = "Finance management APIs")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/summary")
    @Operation(operationId = "getFinanceSummary", description = "Retorna o resumo financeiro consolidado.")
    @ApiResponse(responseCode = "200", description = "Resumo financeiro retornado com sucesso.")
    public ResponseEntity<FinanceSummaryDTO> getFinanceSummary() {
        return ResponseEntity.ok(financeService.getFinanceSummary());
    }
}
