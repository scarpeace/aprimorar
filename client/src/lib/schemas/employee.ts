import { z } from "zod"

export const createEmployeeSchema = z.object({
  name: z.string().min(1, "Employee name is required"),
  birthdate: z.string().refine((date) => {
    const d = new Date(date)
    return d < new Date()
  }, "Birthdate must be in the past"),
  pix: z.string().min(1, "PIX is required"),
  contact: z.string().regex(/^\(\d{2}\)\d{5}-\d{4}$/, "Contact must be in format (XX)XXXXX-XXXX"),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF must be in format XXX.XXX.XXX-XX"),
  email: z.string().email("Invalid email address"),
  role: z.enum(["ADMIN", "EMPLOYEE", "STUDENT", "PARENT"]),
})

export const employeeResponseSchema = z.object({
  id: z.string().uuid(),
  name: z.string(),
  birthdate: z.string(),
  pix: z.string(),
  contact: z.string(),
  cpf: z.string(),
  email: z.string().email(),
  role: z.enum(["ADMIN", "EMPLOYEE", "STUDENT", "PARENT"]),
  active: z.boolean(),
  createdAt: z.string(),
  updatedAt: z.string().nullable(),
})

export type CreateEmployeeInput = z.infer<typeof createEmployeeSchema>
export type EmployeeResponse = z.infer<typeof employeeResponseSchema>
