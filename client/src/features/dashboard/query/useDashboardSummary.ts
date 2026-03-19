import { useQuery } from "@tanstack/react-query";
import {
  dashboardApi,
  type DashboardSummaryParams,
} from "@/features/dashboard/api/dashboardApi";
import { dashboardQueryKeys } from "@/features/dashboard/query/dashboardQueryKeys";

export function useDashboardSummary(params: DashboardSummaryParams) {
  return useQuery({
    queryKey: dashboardQueryKeys.summary(params),
    queryFn: () => dashboardApi.getSummary(params),
    enabled: Boolean(params.year && params.month),
  });
}
