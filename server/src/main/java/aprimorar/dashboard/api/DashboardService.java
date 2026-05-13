package aprimorar.dashboard.api;

import aprimorar.dashboard.api.dto.DashboardSummaryResponseDTO;

public interface DashboardService {

    DashboardSummaryResponseDTO getSummary(Integer year, Integer month);
}
