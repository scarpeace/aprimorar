package com.aprimorar.api.domain.dashboard.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.dashboard.DashboardService;
import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController implements DashboardControllerDocs {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Override
    @GetMapping("/summary")
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
