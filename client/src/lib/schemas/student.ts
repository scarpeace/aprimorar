import { z } from "zod"

export const addressSchema = z.object({
  street: z.string().min(1, "Address street is required"),
  number: z.string().min(1, "Address number is required"),
  complement: z.string().optional(),
  district: z.string().min(1, "Address district is required"),
  city: z.string().min(1, "Address city is required"),
  state: z.string().min(1, "Address state is required"),
  zip: z.string().min(1, "Address zip code is required"),
})

export const parentSchema = z.object({
  name: z.string().min(1, "Parent name is required"),
  email: z.string().email("Invalid email address"),
  contact: z.string().regex(/^\(\d{2}\)\d{5}-\d{4}$/, "Contact must be in format (XX)XXXXX-XXXX"),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF must be in format XXX.XXX.XXX-XX"),
})

export const createStudentSchema = z.object({
  name: z.string().min(1, "Student name is required"),
  birthdate: z.string().refine((date) => {
    const d = new Date(date)
    return d < new Date()
  }, "Birthdate must be in the past"),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF must be in format XXX.XXX.XXX-XX"),
  school: z.string().min(1, "School is required"),
  contact: z.string().regex(/^\(\d{2}\)\d{5}-\d{4}$/, "Contact must be in format (XX)XXXXX-XXXX"),
  email: z.string().email("Invalid email address"),
  activity: z.enum(["ENEM", "MENTORIA"]),
  address: addressSchema,
  parentId: z.string().uuid().optional(),
  parent: parentSchema.optional(),
})

export const studentResponseSchema = z.object({
  id: z.string().uuid(),
  name: z.string(),
  contact: z.string(),
  email: z.string().email(),
  cpf: z.string(),
  birthdate: z.string(),
  school: z.string(),
  activity: z.enum(["ENEM", "MENTORIA"]),
  active: z.boolean(),
  address: addressSchema.nullable(),
  parent: parentSchema.nullable(),
  createdAt: z.string(),
})

export type CreateStudentInput = z.infer<typeof createStudentSchema>
export type StudentResponse = z.infer<typeof studentResponseSchema>
export type AddressInput = z.infer<typeof addressSchema>
export type ParentInput = z.infer<typeof parentSchema>
