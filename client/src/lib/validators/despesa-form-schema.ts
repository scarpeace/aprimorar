import { z } from "zod/v4";
import { despesaRequestSchema } from "@/lib/api/generated/zod";

export const despesaFormSchema = despesaRequestSchema.extend({
  titulo: z.string().trim().min(1, "Título é obrigatório").max(120, "Título deve ter no máximo 120 caracteres"),
  valor: z.number({ error: "Valor é obrigatório" }).min(0.01, "Valor deve ser maior que zero"),
  dataPagamento: z.union([z.iso.date(), z.literal("")]).optional(),
  descricao: z.string().trim().max(500, "Descrição deve ter no máximo 500 caracteres").optional(),
});

export type DespesaFormData = z.infer<typeof despesaFormSchema>;
