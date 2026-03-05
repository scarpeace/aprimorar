import { z } from "zod"

export const eventContentValues = ["AULA", "MENTORIA", "TERAPIA", "ORIENTACAO_VOCACIONAL", "ENEM", "PAS", "OUTRO"] as const

export const eventContentLabels: Record<EventContent, string> = {
  AULA: "AULA",
  TERAPIA: "TERAPIA",
  MENTORIA: "MENTORIA",
  ORIENTACAO_VOCACIONAL: "ORIENTAÇÃO VOCACIONAL",
  ENEM: "ENEM",
  PAS: "PAS",
  OUTRO: "OUTRO",
}

export const createEventSchema = z.object({
  title: z.string().min(1, "Título é obrigatório").max(100, "Título deve ter no máximo 100 caracteres"),
  description: z.string().max(500, "Descrição deve ter no máximo 500 caracteres").optional(),
  startDateTime: z.string().min(1, "Data/hora de início é obrigatório"),
  endDateTime: z.string().min(1, "Data/hora de fim é obrigatório"),
  price: z.number().min(0, "Preço deve ser maior ou igual a 0"),
  payment: z.number().min(0, "Pagamento deve ser maior ou igual a 0"),
  content: z.enum(eventContentValues),
  studentId: z.uuid("ID do aluno inválido"),
  employeeId: z.uuid("ID do colaborador inválido"),
}).refine((data) => data.payment <= data.price, {
  message: "Pagamento não pode ser maior que o preço",
  path: ["payment"],
}).refine((data) => new Date(data.endDateTime) > new Date(data.startDateTime), {
  message: "Fim deve ser depois do início",
  path: ["endDateTime"],
})

export const eventResponseSchema = z.object({
  id: z.number(),
  title: z.string(),
  description: z.string().nullable(),
  startDateTime: z.string(),
  endDateTime: z.string(),
  price: z.number(),
  payment: z.number(),
  content: z.enum(eventContentValues),
  studentId: z.uuid(),
  studentName: z.string(),
  employeeId: z.uuid(),
  employeeName: z.string(),
  createdAt: z.string(),
  updatedAt: z.string().nullable(),
})

export type CreateEventInput = z.infer<typeof createEventSchema>
export type EventResponse = z.infer<typeof eventResponseSchema>
export type EventContent = (typeof eventContentValues)[number]
