import { generalExpenseRequestDTOSchema } from "@/kubb";
import { z } from "zod/v4";

export const generalExpenseFormSchema = generalExpenseRequestDTOSchema.extend({
  amount: z.number().min(0.01, { message: "O valor deve ser maior que zero" }),
  date: z.string().min(1, { message: "A data é obrigatória" }),
  category: z.enum([
    "CONTAS",
    "ADMINISTRATIVO",
    "DESPENSA",
    "MANUTENCAO",
    "SERVICOS",
    "MATERIAIS",
    "ASSINATURAS",
  ]),
});

export type GeneralExpenseFormSchema = z.input<typeof generalExpenseFormSchema>;
