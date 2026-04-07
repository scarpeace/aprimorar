import { eventRequestDTOContentEnum, eventRequestDTOSchema } from "@/kubb";
import z from "zod";

export const eventFormSchema = eventRequestDTOSchema.extend({
  title: z.string().min(1,{message: "O título do evento é obrigatório" }),
  startDate: z.string().min(1,{message: "A data de início do evento é obrigatória" }),
  endDate: z.string().min(1,{message: "A data de término do evento é obrigatória" }),
  price: z.number().min(1,{message: "O preço do evento é obrigatório" }),
  payment: z.number().min(1,{message: "O pagamento do evento é obrigatório" }),
  content: z.enum(eventRequestDTOContentEnum,{message: "O tipo do evento é obrigatório" }),
  studentId: z.string().min(1,{message: "O aluno é obrigatório" }),
  employeeId: z.string().min(1,{message: "O funcionário é obrigatório" }),
});

export type EventFormSchema = z.input<typeof eventFormSchema>;
