import { eventRequestDTOContentEnum } from "@/kubb";
import z from "zod";
import { eventContentLabels } from "./eventContentLabels";

export const eventCreateSchema = z.object({
  title: z.string({ message: "O título do evento é obrigatório" }),
  description: z.string({ message: "A descrição do evento é obrigatória" }),
  startDate: z.date({ message: "A data de início do evento é obrigatória" }),
  endDate: z.date({ message: "A data de término do evento é obrigatória" }),
  price: z.number({ message: "O preço do evento é obrigatório" }),
  payment: z.string({ message: "O pagamento do evento é obrigatório" }),
  content: z.string({ message: "O conteúdo do evento é obrigatório" }),
  studentId: z.string({ message: "O ID do aluno é obrigatório" }),
  employeeId: z.string({ message: "O ID do funcionário é obrigatório" }),
});

export const eventUpdateSchema = z.object({
  title: z.string({ message: "O título do evento é obrigatório" }),
  description: z.string({ message: "A descrição do evento é obrigatória" }),
  startDate: z.date({ message: "A data de início do evento é obrigatória" }),
  endDate: z.date({ message: "A data de término do evento é obrigatória" }),
  price: z.number({ message: "O preço do evento é obrigatório" }),
  payment: z.string({ message: "O pagamento do evento é obrigatório" }),
  content: z.enum(eventContentLabels, { message: "O conteúdo do evento é obrigatório" }),
  studentId: z.string({ message: "O ID do aluno é obrigatório" }),
  employeeId: z.string({ message: "O ID do funcionário é obrigatório" }),
});

export const eventResponseSchema = z.object({
  id: z.string(),
  title: z.string(),
  description: z.string(),
  startDate: z.date(),
  endDate: z.date(),
  price: z.number(),
  payment: z.string(),
  content: z.string(),
  studentId: z.string(),
  employeeId: z.string(),
  createdAt: z.string(),
  updatedAt: z.string().nullable(),
});


export type EventCreateSchema = z.input<typeof eventCreateSchema>;
export type EventUpdateSchema = z.input<typeof eventUpdateSchema>;
export type EventResponseSchema = z.input<typeof eventResponseSchema>;
