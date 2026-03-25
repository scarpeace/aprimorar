import { keepPreviousData } from "@tanstack/react-query";
import {
  useGetDashboardSummary,
  type GetDashboardSummaryQueryParams,
} from "@/kubb";

import { dashboardQueryKeys } from "./dashboardQueryKeys";

export function useDashboardSummary(params: GetDashboardSummaryQueryParams) {
  return useGetDashboardSummary(params, {
    query: {
      queryKey: dashboardQueryKeys.summary(),
      placeholderData: keepPreviousData,
      staleTime: 1000 * 60 * 5, // 5 minutos
    },
  });
}
