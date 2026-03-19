import { z } from "zod"
import { parentResponseSchema } from "../../../lib/schemas/parent"
import { addressResponseSchema, addressFormSchema } from "../../../lib/schemas/address"
import { formatCpf, formatPhone } from "../../../lib/shared/formatter"

export const studentInputSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório"),
  birthdate: z.string().refine((date) => {
    const d = new Date(date)
    return d < new Date()
  }, "Data de nascimento deve estar no passado"),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF deve estar no formato XXX.XXX.XXX-XX"),
  school: z.string().min(1, "Escola é obrigatória"),
  contact: z
    .string()
    .regex(
      /^\(\d{2}\)\s?\d{4,5}-\d{4}$/,
      "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX"
    ).min(1, "O telefone é obrigatório"),
  email: z.email("E-mail inválido"),
  address: addressFormSchema,
  parentId: z.uuid("Responsável é obrigatório"),
})

export const studentResponse = z.object({
  id: z.uuid(),
  name: z.string(),
  birthdate: z.string(),
  contact: z.string().transform(formatPhone),
  email: z.email(),
  cpf: z.string().transform(formatCpf),
  age: z.number().int().nonnegative(),
  school: z.string(),
  address: addressResponseSchema,
  parent: parentResponseSchema,
  archivedAt: z.coerce.date().nullable(),
  createdAt: z.coerce.date(),
  updatedAt: z.coerce.date().nullable(),
})

export const studentOptionSchema = z.object({
  id: z.uuid(),
  name: z.string(),
})

export type StudentFormInput = z.infer<typeof studentInputSchema>
export type StudentResponse = z.infer<typeof studentResponse>
export type StudentOption = z.infer<typeof studentOptionSchema>
