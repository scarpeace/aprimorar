import { parentRequestDTOSchema } from "@/kubb";
import z from "zod";

// Esses schemas do ZOD fora da pasta do KUBB só existem porque não tem como
//  customizar as mensagens de erro através das anotações do backend
export const parentFormSchema = parentRequestDTOSchema.extend({
  name: z.string().min(1, { message: "Nome do responsável é obrigatório" }),
  email: z.string().min(1, { message: "Email do responsável é obrigatório" }),
  contact: z.string().min(1, { message: "Contato do responsável é obrigatório" }),
  birthdate: z.string().min(1, { message: "Data de nascimento é obrigatória" })
    .refine((value) => /^(\d{2})\/(\d{2})\/(\d{4})$/.test(value), {
      message: "Data de nascimento inválida",
    })
    .transform((value) => {
      const [day, month, year] = value.split("/");
      return `${year}-${month}-${day}`;
    }),
  pix: z.string().min(1, { message: "Pix do responsável é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF do responsável é obrigatório" }),
});

export type ParentFormSchema = z.input<typeof parentFormSchema>;
