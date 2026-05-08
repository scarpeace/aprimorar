package com.aprimorar.api.domain.dashboard.api;

import com.aprimorar.api.domain.dashboard.api.dto.DashboardSummaryResponseDTO;

public interface DashboardService {

    DashboardSummaryResponseDTO getSummary(Integer year, Integer month);
}
