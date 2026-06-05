import {
  atendimentoRequestDTOContentEnum,
  type AtendimentoRequestDTOContentEnumKey,
} from "@/kubb";
import { z } from "zod/v4";

const atendimentoContentOptions = Object.values(
  atendimentoRequestDTOContentEnum,
) as [AtendimentoRequestDTOContentEnumKey, ...AtendimentoRequestDTOContentEnumKey[]];

export const atendimentoFormSchema = z.object({
  description: z.string().optional(),
  content: z.enum(atendimentoContentOptions, {
    error: "O tipo do atendimento é obrigatório",
  }),
  startDate: z
    .string()
    .min(1, { message: "A data de início do atendimento é obrigatória" })
    .refine((value) => new Date(value).getTime() > Date.now(), {
      message: "A data/hora de início deve ser no futuro",
    }),
  duration: z
    .number({ error: "A duração do atendimento é obrigatória" })
    .min(0.5, { message: "A duração mínima é de 30 minutos (0.5h)" }),
  price: z
    .number({ error: "O preço do atendimento é obrigatório" })
    .min(0, { message: "O preço deve ser maior ou igual a zero" }),
  payment: z
    .number({ error: "O pagamento do atendimento é obrigatório" })
    .min(0, { message: "O pagamento deve ser maior ou igual a zero" }),
  studentId: z.string().min(1, { message: "O aluno é obrigatório" }),
  employeeId: z.string().min(1, { message: "O colaborador é obrigatório" }),
});

export type AtendimentoFormSchema = z.input<typeof atendimentoFormSchema>;
