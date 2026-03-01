import { z } from "zod"

export const parentSchema = z.object({
  name: z.string().min(1, "Parent name is required"),
  email: z.email("Invalid email address"),
  contact: z.string().regex(/^\(\d{2}\)\d{5}-\d{4}$/, "Contact must be in format (XX)XXXXX-XXXX"),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF must be in format XXX.XXX.XXX-XX"),
})

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
