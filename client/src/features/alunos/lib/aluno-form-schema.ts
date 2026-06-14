import { addressFormSchema } from "@/lib/shared/address/forms/addressSchema";
import { z } from "zod/v4";

const dataNascimentoSchema = z.string()
  .min(1, { message: "Data de nascimento é obrigatória" })
  .refine((value) => /^(\d{2})\/(\d{2})\/(\d{4})$/.test(value), {
    message: "Data de nascimento inválida",
  })
  .transform((value) => {
    const [day, month, year] = value.split("/");
    return `${year}-${month}-${day}`;
  });

export const alunoFormSchema = z.object({
  nome: z.string().min(1, { message: "Nome do aluno é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF é obrigatório" }),
  dataNascimento: dataNascimentoSchema,
  telefone: z.string().min(1, { message: "Telefone é obrigatório" }),
  email: z.string().min(1, { message: "Email é obrigatório" }).email("Email inválido"),
  escola: z.string().min(1, { message: "Escola é obrigatória" }),
  endereco: addressFormSchema,
  responsavelId: z.string()
    .min(1, { message: "Um aluno não pode ser cadastrado sem um responsável" })
    .uuid({ message: "Responsável inválido" }),
});

export type AlunoFormSchema = z.input<typeof alunoFormSchema>;
