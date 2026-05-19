import {
  appointmentRequestDTOContentEnum,
  type AppointmentRequestDTOContentEnumKey,
} from "@/kubb";
import { z } from "zod/v4";

const appointmentContentOptions = Object.values(
  appointmentRequestDTOContentEnum,
) as [AppointmentRequestDTOContentEnumKey, ...AppointmentRequestDTOContentEnumKey[]];

export const appointmentFormSchema = z.object({
  description: z.string().optional(),
  content: z.enum(appointmentContentOptions, {
    error: "O tipo do evento é obrigatório",
  }),
  startDate: z.string().min(1, { message: "A data de início do evento é obrigatória" }),
  duration: z.number({ error: "A duração do evento é obrigatória" }).min(0.5, { message: "A duração mínima é de 30 minutos (0.5h)" }),
  price: z
    .number({ error: "O preço do evento é obrigatório" })
    .min(0, { message: "O preço deve ser maior ou igual a zero" }),
  payment: z
    .number({ error: "O pagamento do evento é obrigatório" })
    .min(0, { message: "O pagamento deve ser maior ou igual a zero" }),
  studentId: z.string().min(1, { message: "O aluno é obrigatório" }),
  employeeId: z.string().min(1, { message: "O funcionário é obrigatório" }),
});

export type AppointmentFormSchema = z.input<typeof appointmentFormSchema>;
