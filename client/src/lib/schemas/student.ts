import { z } from "zod"
import { parentInputSchema, parentResponseSchema } from "./parent"

export const addressSchema = z.object({
  street: z.string().min(1, "Rua é obrigatória"),
  number: z.string().min(1, "Número é obrigatório"),
  complement: z.string().optional(),
  district: z.string().min(1, "Bairro é obrigatório"),
  city: z.string().min(1, "Cidade é obrigatória"),
  state: z.string().min(1, "Estado é obrigatório"),
  zip: z.string().regex(/^\d{5}-?\d{3}$/, "CEP deve estar no formato 00000-000 ou 00000000"),
})

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
  email: z.email("Email inválido"),
  address: addressSchema,
  parentId: z.uuid("Selecione um responsável").optional(),
  parent: parentInputSchema.optional(),
}).refine((data) => (data.parentId ? !data.parent : !!data.parent), {
  message: "Informe um responsável (existente ou novo)",
  path: ["parentId"],
})

export const studentResponseSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  contact: z.string(),
  email: z.email(),
  cpf: z.string(),
  birthdate: z.string(),
  age: z.number().int().nonnegative(),
  school: z.string(),
  archivedAt: z.string().nullable(),
  lastReactivatedAt: z.string().nullable(),
  address: addressSchema.nullable(),
  parent: parentResponseSchema.nullable(),
  createdAt: z.string(),
})

export type CreateStudentInput = z.infer<typeof createStudentSchema>
export type StudentResponse = z.infer<typeof studentResponseSchema>
