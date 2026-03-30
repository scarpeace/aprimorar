import { addressInputSchema, addressResponseSchema } from "@/features/address/hooks/addressSchema";
import { parentInputSchema, parentResponseSchema } from "@/features/parents/hooks/parentSchema";
import { formatDateShortYear } from "@/lib/utils/formatter";
import z from "zod";

export const studentInputSchema = z.object({
  name: z.string().min(1, {message: "Nome do aluno é obrigatório"}),
  birthdate: z.string().min(1, { message: "Data de nascimento é obrigatória" }),
  cpf: z.string().min(1, {message: "CPF é obrigatório"}),
  contact: z.string().min(1, {message: "Contato é obrigatório"}),
  email: z.string().min(1, {message: "Email é obrigatório"}),
  school: z.string().min(1, { message: "Escola é obrigatória" }),
  address: addressInputSchema,
  parent: parentInputSchema,
})

export const studentResponseSchema = z.object({
  id: z.string(),
  name: z.string(),
  birthdate: z.string(),
  cpf: z.string(),
  contact: z.string(),
  email: z.string(),
  school: z.string(),
  age: z.number(),
  address: addressResponseSchema,
  parent: parentResponseSchema,
  archivedAt: z.string().nullable().optional(),
  createdAt: z.string(),
  updatedAt: z.string().nullable().optional(),
});

export type StudentInputSchema = z.input<typeof studentInputSchema>;
export type StudentResponseSchema = z.input<typeof studentResponseSchema>;
