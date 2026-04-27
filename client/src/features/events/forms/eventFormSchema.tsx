import {
  eventRequestDTOContentEnum,
  type EventRequestDTOContentEnumKey,
} from "@/kubb";
import { z } from "zod/v4";

const eventContentOptions = Object.values(
  eventRequestDTOContentEnum,
) as [EventRequestDTOContentEnumKey, ...EventRequestDTOContentEnumKey[]];

export const eventFormSchema = z.object({
  description: z.string().optional(),
  content: z.enum(eventContentOptions, {
    error: "O tipo do evento é obrigatório",
  }),
  startDate: z.string().min(1, { message: "A data de início do evento é obrigatória" }),
  endDate: z.string().min(1, { message: "A data de término do evento é obrigatória" }),
  price: z
    .number({ error: "O preço do evento é obrigatório" })
    .min(0, { message: "O preço deve ser maior ou igual a zero" }),
  payment: z
    .number({ error: "O pagamento do evento é obrigatório" })
    .min(0, { message: "O pagamento deve ser maior ou igual a zero" }),
  studentId: z.string().min(1, { message: "O aluno é obrigatório" }),
  employeeId: z.string().min(1, { message: "O funcionário é obrigatório" }),
});

export type EventFormSchema = z.input<typeof eventFormSchema>;
