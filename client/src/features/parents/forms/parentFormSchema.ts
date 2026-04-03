import { parentRequestDTOSchema } from "@/kubb";
import z from "zod";

// Esses schemas do ZOD fora da pasta do KUBB só existem porque não tem como
//  customizar as mensagens de erro através das anotações do backend
export const parentFormInputSchema = parentRequestDTOSchema.extend({
  name: z.string().min(1, { message: "Nome do pai é obrigatório" }),
  email: z.string().min(1, { message: "Email do pai é obrigatório" }),
  contact: z.string().min(1, { message: "Contato do pai é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF do pai é obrigatório" }),
});

export type ParentFormInputSchema = z.input<typeof parentFormInputSchema>;
