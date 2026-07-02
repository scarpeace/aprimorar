import { z } from "zod/v4";
import { colaboradorRequestDTOSchema, enderecoRequestDTOSchema } from "@/lib/api/generated/zod";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);

const enderecoFormSchema = enderecoRequestDTOSchema.extend({
  rua: requiredText("Rua"),
  numero: requiredText("Número"),
  bairro: requiredText("Bairro"),
  cidade: requiredText("Cidade"),
  estado: requiredText("Estado"),
  cep: requiredText("CEP"),
});

export const colaboradorFormSchema = colaboradorRequestDTOSchema.extend({
  nome: requiredText("Nome"),
  dataNascimento: requiredText("Data de nascimento"),
  pix: requiredText("PIX"),
  telefone: requiredText("Telefone"),
  cpf: requiredText("CPF"),
  email: requiredText("E-mail").email("E-mail inválido"),
  funcao: z.enum(["PROFESSOR", "ADMINISTRATIVO", "TERAPEUTA", "MENTOR"], {
    error: "Função é obrigatória",
  }),
  endereco: enderecoFormSchema,
});

export type ColaboradorFormData = z.infer<typeof colaboradorFormSchema>;
