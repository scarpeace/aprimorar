import { z } from "zod"
import { createParentSchema, parentResponseSchema } from "./parent"
import { addressResponseSchema, createAddressSchema } from "./address"
import { formatCpf, formatDateShortYear, formatPhone } from "../shared/formatter"

export const createStudentSchema = z.object({
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
  ),
  email: z.email("E-mail inválido"),
  address: createAddressSchema,
  parent: createParentSchema.optional(),
})

export const studentResponseSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  contact: z.string().transform(formatPhone),
  email: z.email(),
  cpf: z.string().transform(formatCpf),
  birthdate: z.coerce.date().transform(formatDateShortYear),
  age: z.number().int().nonnegative(),
  school: z.string(),
  archivedAt: z.coerce.date().nullable(),
  address: addressResponseSchema,
  parent: parentResponseSchema,
  createdAt: z.coerce.date().transform(formatDateShortYear),
  updatedAt: z.coerce.date().nullable(),
})

export type CreateStudentInput = z.infer<typeof createStudentSchema>
export type StudentResponse = z.infer<typeof studentResponseSchema>
