package com.aprimorar.api.domain.dashboard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/dashboard")
@Tag(name = "Dashboard", description = "APIs do dashboard operacional")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(
        operationId = "getDashboardSummary",
        summary = "Obter resumo do dashboard",
        description = "Retorna KPIs mensais (alunos ativos, aulas, receita, custo), " +
            "distribuição por tipo de conteúdo e ponteiros de navegação para o mês anterior e próximo."
    )
    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponseDTO> getSummary(
        @Parameter(description = "Ano do período (ex: 2026)", example = "2026")
        @RequestParam Integer year,
        @Parameter(description = "Mês do período (1-12)", example = "3")
        @RequestParam Integer month
    ) {
        DashboardSummaryResponseDTO summary = dashboardService.getSummary(year,month);
        return ResponseEntity.ok(summary);
    }
}
