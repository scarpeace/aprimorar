import { addressFormSchema } from "@/features/address/schemas/addressFormSchema";
import { parentFormSchema } from "@/features/parents/schemas/parentFormSchema";
import { studentRequestDTOSchema } from "@/kubb";
import { z } from "@/lib/validations/zod";

export const studentFormSchema = studentRequestDTOSchema.extend({
  name: z
    .string()
    .min(10, { message: "Nome do aluno deve ter no mínimo 10 caracteres" })
    .max(120, { message: "Nome do aluno deve ter no máximo 120 caracteres" }),
  school: z.string().min(1, { message: "Escola do aluno é obrigatória" }),
  contact: z.string().min(1, { message: "Contato do aluno é obrigatório" }),
  email: z
    .string()
    .min(1, { message: "Email do aluno é obrigatório" })
    .email({ message: "Email deve ser um endereço de email válido" }),
  address: addressFormSchema,
  parent: parentFormSchema,
});

export type StudentFormInput = z.input<typeof studentFormSchema>;
