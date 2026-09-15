import { z } from "zod/v4";
import { colaboradorRequestDTOSchema } from "@/lib/api/generated/zod";
import { enderecoFormSchema } from "@/lib/schemas/endereco-form-schema";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);


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
