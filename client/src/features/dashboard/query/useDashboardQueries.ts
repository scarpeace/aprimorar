import { useQuery } from "@tanstack/react-query";
import { fetchDashboardSummary } from "@/features/dashboard/api/dashboardApi";
import { dashboardQueryKeys } from "@/features/dashboard/query/dashboardQueryKeys";
interface DashboardSummaryParams {
  year: number;
  month: number;
}
export function useDashboardSummaryQuery(params: DashboardSummaryParams) {
  return useQuery({
    queryKey: dashboardQueryKeys.summary(params),
    queryFn: () => fetchDashboardSummary(params),
    enabled: Boolean(params.year && params.month),
    refetchInterval: 60_000,
  });
}
