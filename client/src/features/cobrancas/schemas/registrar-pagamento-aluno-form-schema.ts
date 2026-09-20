import { z } from "zod/v4";
import { registrarPagamentoAlunoRequestSchema } from "@/lib/api/generated/zod/registrarPagamentoAlunoRequestSchema";

const requestSchema = registrarPagamentoAlunoRequestSchema.omit({ comprovanteUrl: true });

export const registrarPagamentoAlunoFormSchema = requestSchema.extend({
  cobrancaIds: requestSchema.shape.cobrancaIds.min(1, "Selecione ao menos uma cobrança"),
});

export type RegistrarPagamentoAlunoFormData = z.infer<typeof registrarPagamentoAlunoFormSchema>;
