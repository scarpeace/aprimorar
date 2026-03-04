import { z } from "zod"

export const parentSchema = z.object({
  name: z.string().min(1, "Nome do responsavel e obrigatorio"),
  email: z.email("Email do responsavel invalido"),
  contact: z
    .string()
    .regex(
      /^\(\d{2}\)\s?\d{4,5}-\d{4}$/,
      "Contato do responsavel deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX"
    ),
  cpf: z
    .string()
    .regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF do responsavel deve estar no formato XXX.XXX.XXX-XX"),
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
