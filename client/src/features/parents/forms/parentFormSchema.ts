import { parentRequestDTOSchema } from "@/kubb";
import z from "zod";

// Esses schemas do ZOD fora da pasta do KUBB só existem porque não tem como
//  customizar as mensagens de erro através das anotações do backend
export const parentFormSchema = parentRequestDTOSchema.extend({
  name: z.string().min(1, { message: "Nome do responsável é obrigatório" }),
  email: z.string().min(1, { message: "Email do responsável é obrigatório" }),
  contact: z.string().min(1, { message: "Contato do responsável é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF do responsável é obrigatório" }),
});

export type ParentFormSchema = z.input<typeof parentFormSchema>;
