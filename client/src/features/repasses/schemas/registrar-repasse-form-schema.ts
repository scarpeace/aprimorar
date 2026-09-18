import { z } from "zod/v4";
import { registrarRepasseIndividualRequestSchema } from "@/lib/api/generated/zod/registrarRepasseIndividualRequestSchema";

const requestSchema = registrarRepasseIndividualRequestSchema.omit({ comprovanteUrl: true });

export const registrarRepasseFormSchema = requestSchema.extend({
  repasseIds: requestSchema.shape.repasseIds.min(1, "Selecione ao menos um repasse"),
});

export type RegistrarRepasseFormData = z.infer<typeof registrarRepasseFormSchema>;
