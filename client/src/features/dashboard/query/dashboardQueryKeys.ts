export interface DashboardSummaryParams {
  year: number;
  month: number;
}
export const dashboardQueryKeys = {
  all: ["dashboard"] as const,
  summary: (params: DashboardSummaryParams) =>["dashboard", "summary", params] as const,
} as const;