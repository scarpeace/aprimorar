package aprimorar.dashboard.internal;

import aprimorar.dashboard.api.dto.DashboardSummaryResponseDTO;

interface DashboardQueryService {

    DashboardSummaryResponseDTO getSummary(Integer year, Integer month);
}
