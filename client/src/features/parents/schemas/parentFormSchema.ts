import { parentRequestDTOSchema } from "@/kubb";
import { z } from "@/lib/validations/zod";

export const parentFormSchema = parentRequestDTOSchema.extend({
  name: z.string().min(1, { message: "Nome do responsável é obrigatório" }),
  email: z
    .string()
    .min(1, { message: "Email do responsável é obrigatório" })
    .email({ message: "Email deve ser um endereço de email válido" }),
  contact: z.string().min(1, { message: "Contato do responsável é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF do responsável é obrigatório" }),
});

export type ParentFormInput = z.input<typeof parentFormSchema>;
