import { api } from "@/lib/shared/api";
import {
  dashboardSummaryResponseSchema,
  type DashboardSummaryResponse,
} from "@/features/dashboard/schemas/dashboardSummary";

export interface DashboardSummaryParams {
  year: number;
  month: number;
}

export const dashboardApi = {
  async getSummary(
    params: DashboardSummaryParams,
  ): Promise<DashboardSummaryResponse> {
    const { data } = await api.get<DashboardSummaryResponse>(
      "/v1/dashboard/summary",
      {
        params,
      },
    );

    return dashboardSummaryResponseSchema.parse(data);
  },
};
