import { z } from "zod"

export const monthPointerSchema = z.object({
  year: z.number().int(),
  month: z.number().int().min(1).max(12),
})

export const dashboardPeriodSchema = z.object({
  year: z.number().int(),
  month: z.number().int().min(1).max(12),
  prev: monthPointerSchema,
  next: monthPointerSchema,
})

export const dashboardKpisSchema = z.object({
  activeStudentsInMonth: z.number().int().nonnegative(),
  classesInMonth: z.number().int().nonnegative(),
  revenueInMonth: z.coerce.number(),
  costInMonth: z.coerce.number(),
})

export const classesByContentSchema = z.object({
  content: z.string(),
  count: z.number().int().nonnegative(),
  percentage: z.coerce.number(),
})

export const dashboardChartsSchema = z.object({
  classesByContent: z.array(classesByContentSchema),
})

export const dashboardMetaSchema = z.object({
  generatedAt: z.coerce.date(),
  refreshSeconds: z.number().int().positive(),
})

export const dashboardSummaryResponseSchema = z.object({
  period: dashboardPeriodSchema,
  kpis: dashboardKpisSchema,
  charts: dashboardChartsSchema,
  meta: dashboardMetaSchema,
})

export type MonthPointer = z.infer<typeof monthPointerSchema>
export type DashboardPeriod = z.infer<typeof dashboardPeriodSchema>
export type DashboardKpis = z.infer<typeof dashboardKpisSchema>
export type ClassesByContent = z.infer<typeof classesByContentSchema>
export type DashboardCharts = z.infer<typeof dashboardChartsSchema>
export type DashboardMeta = z.infer<typeof dashboardMetaSchema>
export type DashboardSummaryResponse = z.infer<typeof dashboardSummaryResponseSchema>
