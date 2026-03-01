import { z } from "zod"

export const parentSummarySchema = z.object({
  id: z.uuid(),
  name: z.string(),
})

export const parentResponseSchema = z.object({
  name: z.string(),
  email: z.string(),
  contact: z.string(),
  cpf: z.string(),
})

export type ParentSummary = z.infer<typeof parentSummarySchema>
export type ParentResponse = z.infer<typeof parentResponseSchema>
