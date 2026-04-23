import {
  eventRequestDTOContentEnum,
  eventRequestDTOStatusEnum,
  eventRequestDTOSchema,
} from "@/kubb";
import z from "zod";

export const eventFormSchema = eventRequestDTOSchema.extend({
  startDate: z
    .string()
    .min(1, { message: "A data de início do evento é obrigatória" }),
  endDate: z
    .string()
    .min(1, { message: "A data de término do evento é obrigatória" }),
  price: z.number({
    required_error: "O preço do evento é obrigatório",
    invalid_type_error: "O preço do evento é obrigatório",
  }).min(0, { message: "O preço deve ser maior ou igual a zero" }),
  payment: z.number({
    required_error: "O pagamento do evento é obrigatório",
    invalid_type_error: "O pagamento do evento é obrigatório",
  }).min(0, { message: "O pagamento deve ser maior ou igual a zero" }),
  content: z.enum(eventRequestDTOContentEnum, {
    required_error: "O tipo do evento é obrigatório",
  }),
  studentId: z.string().min(1, { message: "O aluno é obrigatório" }),
  employeeId: z.string().min(1, { message: "O funcionário é obrigatório" }),
  status: z
    .enum(eventRequestDTOStatusEnum, {
      required_error: "O status do evento é obrigatório",
    })
    .default("SCHEDULED"),
});

export type EventFormSchema = z.input<typeof eventFormSchema>;
