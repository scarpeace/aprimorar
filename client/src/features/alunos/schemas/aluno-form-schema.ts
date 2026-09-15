import { z } from "zod/v4";
import { alunoRequestDTOSchema } from "@/lib/api/generated/zod";
import { enderecoFormSchema } from "@/lib/schemas/endereco-form-schema";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);


export const alunoFormSchema = alunoRequestDTOSchema.extend({
  nome: requiredText("Nome"),
  dataNascimento: requiredText("Data de nascimento"),
  cpf: requiredText("CPF"),
  escola: requiredText("Escola"),
  telefone: requiredText("Telefone"),
  email: requiredText("E-mail").email("E-mail inválido"),
  responsavelId: requiredText("Responsável"),
  endereco: enderecoFormSchema,
});

export type AlunoFormData = z.infer<typeof alunoFormSchema>;
