import {
  colaboradorRequestDTOFuncaoEnum,
  type ColaboradorRequestDTOFuncaoEnumKey,
} from "@/kubb";
import { z } from "zod/v4";

const funcaoOptions = Object.values(colaboradorRequestDTOFuncaoEnum) as [
  ColaboradorRequestDTOFuncaoEnumKey,
  ...ColaboradorRequestDTOFuncaoEnumKey[],
];

const dataNascimentoSchema = z.string()
  .min(1, { message: "Data de nascimento é obrigatória" })
  .refine((value) => /^(\d{2})\/(\d{2})\/(\d{4})$/.test(value), {
    message: "Data de nascimento inválida",
  })
  .transform((value) => {
    const [day, month, year] = value.split("/");
    return `${year}-${month}-${day}`;
  });

const enderecoFormSchema = z.object({
  rua: z.string().min(1, { message: "Rua é obrigatória" }),
  numero: z.string().optional(),
  bairro: z.string().min(1, { message: "Bairro é obrigatório" }),
  cidade: z.string().min(1, { message: "Cidade é obrigatória" }),
  estado: z.string().min(1, { message: "Estado é obrigatório" }),
  cep: z.string().min(1, { message: "CEP é obrigatório" }),
  complemento: z.string().optional(),
});

export const colaboradorFormSchema = z.object({
  nome: z.string().min(1, { message: "Nome do colaborador é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF é obrigatório" }),
  dataNascimento: dataNascimentoSchema,
  pix: z.string().min(1, { message: "Pix do colaborador é obrigatório" }),
  telefone: z.string().min(1, { message: "Telefone é obrigatório" }),
  email: z.string().min(1, { message: "Email é obrigatório" }).email("Email inválido"),
  funcao: z.enum(funcaoOptions, { error: "Função do colaborador é obrigatória" }),
  endereco: enderecoFormSchema,
});

export type ColaboradorFormSchema = z.input<typeof colaboradorFormSchema>;
