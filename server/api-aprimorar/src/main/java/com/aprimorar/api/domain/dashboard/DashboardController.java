package com.aprimorar.api.domain.dashboard;

import com.aprimorar.api.domain.dashboard.dto.DashboardSummaryResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummaryResponseDTO> getSummary(
        @RequestParam Integer year,
        @RequestParam Integer month
    ) {
        DashboardSummaryResponseDTO summary = dashboardService.getSummary(
            year,
            month
        );
        return ResponseEntity.ok(summary);
    }
}
