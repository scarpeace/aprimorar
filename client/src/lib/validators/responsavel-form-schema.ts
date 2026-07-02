import { z } from "zod/v4";
import { responsavelRequestDTOSchema } from "@/lib/api/generated/zod";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);

export const responsavelFormSchema = responsavelRequestDTOSchema.extend({
  nome: requiredText("Nome"),
  email: requiredText("E-mail").email("E-mail inválido"),
  telefone: requiredText("Telefone"),
  dataNascimento: z.string().optional(),
  cpf: requiredText("CPF"),
});

export type ResponsavelFormData = z.infer<typeof responsavelFormSchema>;
