import { z } from "zod/v4";
import { alunoRequestDTOSchema, enderecoRequestDTOSchema } from "@/lib/api/generated/zod";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);

const enderecoFormSchema = enderecoRequestDTOSchema.extend({
  rua: requiredText("Rua"),
  numero: requiredText("Número"),
  bairro: requiredText("Bairro"),
  cidade: requiredText("Cidade"),
  estado: requiredText("Estado"),
  cep: requiredText("CEP"),
});

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
