import { z } from "zod"
import { formatPhone, formatCpf, isValidCpf, isValidBrazilianPhone, formatDateShortYear } from '../shared/formatter';

export const parentFormSchema = z.object({
  name: z.string().min(1, "Nome do responsável é obrigatório"),
  email: z.email("E-mail do responsável inválido").min(1, "Email é obrigatório"),
  contact: z.string()
    .min(1, "Contato é obrigatório")
    .refine(isValidBrazilianPhone, "Número de telefone inválido, confira o número informado"),
  cpf: z.string()
    .min(1, "CPF é obrigatório")
    .refine(isValidCpf, "CPF Inválido, confira os números informados")
})

// TODO este resumo de responsável é realmente necessário?
export const parentSummarySchema = z.object({
  id: z.uuid(),
  name: z.string(),
})

export const parentResponseSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  email: z.string(),
  contact: z.string().transform(formatPhone),
  cpf: z.string().transform(formatCpf),
  archivedAt: z.coerce.date().nullable(),
  createdAt: z.coerce.date().transform(formatDateShortYear),
  updatedAt: z.coerce.date().nullable(),
})

export type ParentSummary = z.infer<typeof parentSummarySchema>
export type ParentResponse = z.infer<typeof parentResponseSchema>
export type ParentFormInput = z.infer<typeof parentFormSchema>
