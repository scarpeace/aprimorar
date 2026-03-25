import type { GetStudentsQueryParams } from "@/kubb";

export const dashboardQueryKeys = {
  all: ["students"] as const,
  summary: (params: GetStudentsQueryParams = {}) =>
    ["students", "list", params] as const,

} as const;

export type DashboardSummaryQueryKey = ReturnType<typeof dashboardQueryKeys.summary>;
