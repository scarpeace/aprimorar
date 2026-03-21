import { getDashboardSummary } from "@/gen/client/dashboard/getDashboardSummary";
import { getDashboardSummaryQueryResponseSchema } from "@/gen/schemas/dashboard/getDashboardSummarySchema";

interface DashboardSummaryParams {
  year: number;
  month: number;
}

export async function fetchDashboardSummary(params: DashboardSummaryParams) {
  const raw = await getDashboardSummary(params);
  return getDashboardSummaryQueryResponseSchema.parse(raw);
}