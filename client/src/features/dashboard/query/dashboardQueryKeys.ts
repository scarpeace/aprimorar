export const dashboardQueryKeys = {
  all: ["dashboard"] as const,
  summary: (params: { year: number; month: number }) =>
    ["dashboard", "summary", params] as const,
} as const
