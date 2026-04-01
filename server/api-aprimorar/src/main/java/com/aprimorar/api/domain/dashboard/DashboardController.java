package com.aprimorar.api.domain.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @Operation(operationId = "getDashboardSummary", description = "Retorna os indicadores e KPI's do dashboard.")
    @ApiResponse(responseCode = "200", description = "Payload do dashboard retornado com sucesso.")
    public ResponseEntity<DashboardSummaryResponseDTO> getDashboardSummary(
            @Parameter(description = "Ano do período (ex: 2026)", example = "2026")
            @RequestParam Integer year,
            @Parameter(description = "Mês do período (1-12)", example = "3")
            @RequestParam Integer month
    ) {
        DashboardSummaryResponseDTO summary = dashboardService.getSummary(year, month);
        return ResponseEntity.ok(summary);
    }
}
