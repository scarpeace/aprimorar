import { atendimentoRequestTipoEnum } from "@/kubb";
import { z } from "zod/v4";


export const atendimentoFormSchema = z.object({
  descricao: z.string().optional(),
  tipo: z.enum(atendimentoRequestTipoEnum, {
    error: "O tipo do atendimento é obrigatório",
  }),
  inicio: z.string().min(1, { message: "A data de início do atendimento é obrigatória" }),
  duracao: z
    .number({ error: "A duração do atendimento é obrigatória" })
    .min(0.5, { message: "A duração mínima é de 30 minutos (0.5h)" }),
  valor: z
    .number({ error: "O valor do atendimento é obrigatório" })
    .min(0, { message: "O valor deve ser maior ou igual a zero" }),
  repasse: z
    .number({ error: "O repasse do atendimento é obrigatório" })
    .min(0, { message: "O repasse deve ser maior ou igual a zero" }),
  alunoId: z.string().min(1, { message: "O aluno é obrigatório" }),
  colaboradorId: z.string().min(1, { message: "O colaborador é obrigatório" }),
});

export type AtendimentoFormSchema = z.input<typeof atendimentoFormSchema>;
