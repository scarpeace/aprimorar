import { z } from "zod/v4";
import { atendimentoRequestSchema } from "@/lib/api/generated/zod";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);
const requiredUuid = (label: string) => requiredText(label).uuid(`${label} é obrigatório`);

export const atendimentoFormSchema = z.object({
  tipo: atendimentoRequestSchema.shape.tipo,
  dataHoraInicio: requiredText("Data e hora de início"),
  duracao: z.coerce
    .number({ error: "Duração é obrigatória" })
    .min(0.5, "A duração mínima é de 30 minutos"),
  pagamentoAluno: atendimentoRequestSchema.shape.pagamentoAluno,
  repasseColaborador: atendimentoRequestSchema.shape.repasseColaborador,
  alunoId: requiredUuid("Aluno"),
  colaboradorId: requiredUuid("Colaborador"),
});

export type AtendimentoFormData = z.infer<typeof atendimentoFormSchema>;
