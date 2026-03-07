import { z } from "zod"

export const parentInputSchema = z.object({
  name: z.string().min(1, "Nome do responsável é obrigatório"),
  email: z.email("Email do responsável inválido"),
  contact: z
    .string()
    .regex(
      /^\(\d{2}\)\s?\d{4,5}-\d{4}$/,
      "Contato do responsável deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX"
    ),
  cpf: z
    .string()
    .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF do responsável deve estar no formato XXX.XXX.XXX-XX"),
})

export const parentResponseSchema = parentInputSchema.extend({
  id: z.uuid(),
})

export const parentSummarySchema = z.object({
  id: z.uuid(),
  name: z.string(),
})

export type ParentInput = z.infer<typeof parentInputSchema>
export type ParentResponse = z.infer<typeof parentResponseSchema>
export type ParentSummary = z.infer<typeof parentSummarySchema>
