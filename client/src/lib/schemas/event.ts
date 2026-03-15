import { z } from "zod"
import { eventContentValues } from "@/lib/shared/enums"

// TODO Evento não vai precisar de título, ele vai ser gerado automaticamente no backend. Remover num futuro próximo do front.
export const eventInputSchema = z.object({
  title: z.string().min(1, "Título é obrigatório").max(100, "Título deve ter no máximo 100 caracteres"),
  description: z.string().max(500, "Descrição deve ter no máximo 500 caracteres").optional(),
  startDate: z.date().min(1, "Data/hora de início é obrigatório"),
  endDate: z.date().min(1, "Data/hora de fim é obrigatório"),
  price: z.number().min(0, "Preço deve ser maior ou igual a 0"),
  payment: z.number().min(0, "Pagamento deve ser maior ou igual a 0"),
  content: z.enum(eventContentValues),
  studentId: z.uuid("ID do aluno inválido"),
  employeeId: z.uuid("ID do colaborador inválido"),
}).refine((data) => data.payment <= data.price, {
  message: "Pagamento não pode ser maior que o preço",
  path: ["payment"],
}).refine((data) => data.endDate > data.startDate, {
  message: "Fim deve ser depois do início",
  path: ["endDate"],
})

export const eventResponse = z.object({
  id: z.uuid(),
  title: z.string(),
  description: z.string().nullable(),
  startDate: z.coerce.date(),
  endDate: z.coerce.date(),
  price: z.number(),
  payment: z.number(),
  content: z.enum(eventContentValues),
  studentId: z.uuid(),
  studentName: z.string(),
  employeeId: z.uuid(),
  employeeName: z.string(),
  createdAt: z.coerce.date(),
  updatedAt: z.coerce.date().nullable(),
})

export type EventFormInput = z.infer<typeof eventInputSchema>
export type EventResponse = z.infer<typeof eventResponse>
