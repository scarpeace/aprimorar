import { getParentById } from "@/kubb";
import {
  addressRequestDTOSchema,
  studentRequestDTOSchema,
} from "@/kubb/zod";
import { z } from "zod/v4";

const addressFormSchema = addressRequestDTOSchema.extend({
  street: z.string().min(1, { message: "Rua é obrigatória" }),
  number: z.string().min(1, { message: "Número é obrigatório" }),
  district: z.string().min(1, { message: "Bairro é obrigatório" }),
  city: z.string().min(1, { message: "Cidade é obrigatória" }),
  zip: z.string().min(1, { message: "CEP é obrigatório" }),
  complement: z.string().optional(),
});

export const studentFormSchema = studentRequestDTOSchema.extend({
  name: z.string().min(1, { message: "Nome do aluno é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF é obrigatório" }),
  birthdate: z.string().min(1, { message: "Data de nascimento é obrigatória" }),
  contact: z.string().min(1, { message: "Contato é obrigatório" }),
  email: z.string().min(1, { message: "Email é obrigatório" }),
  school: z.string().min(1, { message: "Escola é obrigatória" }),
  address: addressFormSchema,
  getParentById: z.uuid().min(1, { message: "Um aluno não pode ser cadatrado sem um responsável" }),
})

export type StudentFormSchema = z.input<typeof studentFormSchema>;
