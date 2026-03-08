import { z } from "zod"

export const createEmployeeSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório").max(200, "Nome muito grande"),
  birthdate: z.string().refine((date) => {
    const d = new Date(date)
    return d < new Date()
  }, "Data de nascimento deve estar no passado"),
  pix: z.string().min(1, "Chave PIX é obrigatória"),
  contact: z
    .string()
    .regex(
      /^\(\d{2}\)\s?\d{4,5}-\d{4}$/,
      "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX"
    ),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF deve estar no formato XXX.XXX.XXX-XX"),
  email: z.email("E-mail inválido"),
  role: z.enum(["ADMIN", "EMPLOYEE", "STUDENT", "PARENT"]),
})

export const employeeResponseSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  birthdate: z.string(),
  pix: z.string(),
  contact: z.string(),
  cpf: z.string(),
  email: z.email(),
  role: z.enum(["ADMIN", "EMPLOYEE", "STUDENT", "PARENT"]),
  active: z.boolean(),
  createdAt: z.string(),
  updatedAt: z.string().nullable(),
})

export type CreateEmployeeInput = z.infer<typeof createEmployeeSchema>
export type EmployeeResponse = z.infer<typeof employeeResponseSchema>
