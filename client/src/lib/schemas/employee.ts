import { z } from "zod"
import { dutyValues } from "@/lib/shared/enums"
import { formatCpf, formatDateShortYear, formatPhone } from '../shared/formatter';

export const createEmployeeSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório").max(200, "Nome muito grande"),
  birthdate: z.string().refine((date) => {
    const d = new Date(date)
    return d < new Date()
  }, "Data de nascimento deve estar no passado"),
  pix: z.string().min(1, "Chave PIX é obrigatória").max(100, "Chave PIX pode ter somente até 100 caracteres"),
  contact: z
    .string()
    .regex(
      /^\(\d{2}\)\s?\d{4,5}-\d{4}$/,
      "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX"
    ),
  cpf: z.string().regex(/^\d{3}\.\d{3}\.\d{3}-\d{2}$/, "CPF deve estar no formato XXX.XXX.XXX-XX"),
  email: z.email("E-mail inválido").max(254, "E-mail pode ter somente até 254 caracteres"),
  duty: z.enum(dutyValues),
})

export const employeeResponseSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  birthdate: z.coerce.date().transform(formatDateShortYear),
  pix: z.string(),
  contact: z.string().transform(formatPhone),
  cpf: z.string().transform(formatCpf),
  email: z.email(),
  duty: z.enum(dutyValues),
  archivedAt: z.coerce.date().nullable(),
  createdAt: z.coerce.date().transform(formatDateShortYear),
  updatedAt: z.coerce.date().nullable(),
})

export type CreateEmployeeInput = z.infer<typeof createEmployeeSchema>
export type EmployeeResponse = z.infer<typeof employeeResponseSchema>
