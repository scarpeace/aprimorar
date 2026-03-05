import { z } from "zod"

export const eventContentValues = ["FISICA", "MATEMATICA", "TERAPIA", "MENTORIA", "ENEM", "OUTRO"] as const

export const eventContentLabels: Record<EventContent, string> = {
  FISICA: "Física",
  MATEMATICA: "Matematica",
  TERAPIA: "Terapia",
  MENTORIA: "Mentoria",
  ENEM: "Enem",
  OUTRO: "Outro",
}

export const createEventSchema = z.object({
  title: z.string().min(1, "Titulo e obrigatorio").max(100, "Titulo deve ter no maximo 100 caracteres"),
  description: z.string().max(500, "Descricao deve ter no maximo 500 caracteres").optional(),
  startDateTime: z.string().min(1, "Data/hora de inicio e obrigatoria"),
  endDateTime: z.string().min(1, "Data/hora de fim e obrigatoria"),
  price: z.number().min(0, "Preco deve ser maior ou igual a 0"),
  payment: z.number().min(0, "Pagamento deve ser maior ou igual a 0"),
  content: z.enum(eventContentValues),
  studentId: z.uuid("ID do aluno invalido"),
  employeeId: z.uuid("ID do colaborador invalido"),
}).refine((data) => data.payment <= data.price, {
  message: "Pagamento nao pode ser maior que o preco",
  path: ["payment"],
}).refine((data) => new Date(data.endDateTime) > new Date(data.startDateTime), {
  message: "Fim deve ser depois do inicio",
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
