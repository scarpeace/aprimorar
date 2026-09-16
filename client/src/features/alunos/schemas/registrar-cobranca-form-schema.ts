import { z } from "zod/v4";
import { registrarPagamentoIndividualRequestSchema } from "@/lib/api/generated/zod/registrarPagamentoIndividualRequestSchema";

const requestSchema = registrarPagamentoIndividualRequestSchema.omit({ comprovanteUrl: true });

export const registrarCobrancaFormSchema = requestSchema.extend({
  cobrancaIds: requestSchema.shape.cobrancaIds.min(1, "Selecione ao menos uma cobrança"),
});

export type RegistrarCobrancaFormData = z.infer<typeof registrarCobrancaFormSchema>;
