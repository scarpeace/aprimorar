import { z } from "zod/v4";
import { alunoRequestDTOSchema, responsavelRequestDTOSchema } from "@/lib/api/generated/zod";
import { enderecoFormSchema } from "@/lib/schemas/endereco-form-schema";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);

const responsavelFormSchema = responsavelRequestDTOSchema.extend({
  nome: requiredText("Nome do responsável"),
  cpf: requiredText("CPF do responsável"),
  telefone: requiredText("Telefone do responsável"),
  email: requiredText("E-mail do responsável").email("E-mail do responsável inválido"),
});

export const alunoFormSchema = alunoRequestDTOSchema.extend({
  nome: requiredText("Nome"),
  dataNascimento: requiredText("Data de nascimento"),
  cpf: requiredText("CPF"),
  escola: requiredText("Escola"),
  telefone: requiredText("Telefone"),
  email: requiredText("E-mail").email("E-mail inválido"),
  endereco: enderecoFormSchema,
  responsavel: responsavelFormSchema,
});

export type AlunoFormData = z.infer<typeof alunoFormSchema>;
