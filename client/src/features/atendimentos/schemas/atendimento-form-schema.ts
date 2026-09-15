import { z } from "zod/v4";
import { atendimentoRequestSchema } from "@/lib/api/generated/zod";

export const atendimentoFormSchema = z
  .object({
    tipo: atendimentoRequestSchema.shape.tipo,
    dataHoraInicio: z.string().trim().min(1, "Data e hora de início é obrigatória"),
    duracao: z.coerce.number({ error: "Duração é obrigatória" }).min(0.5, "A duração mínima é de 30 minutos"),
    pagamentoAluno: atendimentoRequestSchema.shape.pagamentoAluno,
    repasseColaborador: atendimentoRequestSchema.shape.repasseColaborador,
    alunoId: z.string().trim().uuid("Aluno é obrigatório"),
    colaboradorId: z.string().trim().uuid("Colaborador é obrigatório"),
    recorrente: z.boolean().optional(),
    dataFimRecorrencia: z.string().optional(),
  })
  .refine((data) => !data.recorrente || !!data.dataFimRecorrencia, {
    path: ["dataFimRecorrencia"],
    message: "Data final da recorrência é obrigatória",
  })
  .refine((data) => !data.recorrente || !data.dataFimRecorrencia || data.dataFimRecorrencia >= data.dataHoraInicio.slice(0, 10), {
    path: ["dataFimRecorrencia"],
    message: "Data final da recorrência deve ser igual ou posterior à data inicial",
  });

export type AtendimentoFormInput = z.input<typeof atendimentoFormSchema>;
export type AtendimentoFormData = z.output<typeof atendimentoFormSchema>;
