import { z } from "zod/v4";
import { atendimentoIndividualRequestSchema } from "@/lib/api/generated/zod";

export const atendimentoFormSchema = z.object({
  tipo: atendimentoIndividualRequestSchema.shape.tipo,
  dataHoraInicio: z.string().trim().min(1, "Data e hora de início é obrigatória"),
  duracao: z.coerce.number({ error: "Duração é obrigatória" }).min(0.5, "A duração mínima é de 30 minutos"),
  valorCobranca: atendimentoIndividualRequestSchema.shape.valorCobranca,
  valorRepasse: atendimentoIndividualRequestSchema.shape.valorRepasse,
  alunoId: z.string().trim().uuid("Aluno é obrigatório"),
  colaboradorId: z.string().trim().uuid("Colaborador é obrigatório"),
});

export type AtendimentoFormInput = z.input<typeof atendimentoFormSchema>;
export type AtendimentoFormData = z.output<typeof atendimentoFormSchema>;
