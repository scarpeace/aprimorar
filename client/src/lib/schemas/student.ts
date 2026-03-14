import { z } from "zod"
import { parentFormSchema, parentResponseSchema } from "./parent"
import { addressResponseSchema, addressFormSchema } from "./address"
import { formatCpf, formatDateShortYear, formatPhone } from "../shared/formatter"

export const studentFormSchema = z.object({
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
  parent: parentFormSchema.optional(),
})

export const studentApiSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  contact: z.string().transform(formatPhone),
  email: z.email(),
  cpf: z.string().transform(formatCpf),
  birthdate: z.string(),
  age: z.number().int().nonnegative(),
  school: z.string(),
  archivedAt: z.coerce.date().nullable(),
  address: addressResponseSchema,
  parent: parentResponseSchema,
  createdAt: z.coerce.date(),
  updatedAt: z.coerce.date().nullable(),
})

export const studentResponseSchema = studentApiSchema.transform((student) => ({
  ...student,
  birthdate: formatDateShortYear(student.birthdate),
  createdAt: formatDateShortYear(student.createdAt),
}))

export type StudentFormInput = z.infer<typeof studentFormSchema>
export type StudentApiResponse = z.infer<typeof studentApiSchema>
export type StudentResponse = z.infer<typeof studentResponseSchema>
