import { z } from "zod"
import { parentSchema } from "./parent"

export const addressSchema = z.object({
  street: z.string().min(1, "Rua e obrigatoria"),
  number: z.string().min(1, "Numero e obrigatorio"),
  complement: z.string().optional(),
  district: z.string().min(1, "Bairro e obrigatorio"),
  city: z.string().min(1, "Cidade e obrigatoria"),
  state: z.string().min(1, "Estado e obrigatorio"),
  zip: z.string().min(1, "CEP e obrigatorio"),
})

export const createStudentSchema = z.object({
  name: z.string().min(1, "Nome e obrigatorio"),
  birthdate: z.string().refine((date) => {
    const d = new Date(date)
    return d < new Date()
  }, "Data de nascimento deve estar no passado"),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF deve estar no formato XXX.XXX.XXX-XX"),
  school: z.string().min(1, "Escola e obrigatoria"),
  contact: z
    .string()
    .regex(
      /^\(\d{2}\)\s?\d{4,5}-\d{4}$/,
      "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX"
    ),
  email: z.email("Email invalido"),
  address: addressSchema,
  parentId: z.uuid("Selecione um responsavel").optional(),
  parent: parentSchema.optional(),
}).refine((data) => (data.parentId ? !data.parent : !!data.parent), {
  message: "Informe um responsavel (existente ou novo)",
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
  active: z.boolean(),
  address: addressSchema.nullable(),
  parent: parentSchema.nullable(),
  createdAt: z.string(),
})

export type CreateStudentInput = z.infer<typeof createStudentSchema>
export type StudentResponse = z.infer<typeof studentResponseSchema>
export type AddressInput = z.infer<typeof addressSchema>
export type ParentInput = z.infer<typeof parentSchema>
