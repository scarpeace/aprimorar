import { addressFormSchema } from "@/lib/shared/address/forms/addressSchema.ts";
import {
  colaboradorRequestDTODutyEnum,
  type ColaboradorRequestDTODutyEnumKey,
} from "@/kubb";
import { z } from "zod/v4";

const dutyOptions = Object.values(
  colaboradorRequestDTODutyEnum,
) as [ColaboradorRequestDTODutyEnumKey, ...ColaboradorRequestDTODutyEnumKey[]];

const birthdateSchema = z.string()
  .min(1, { message: "Data de nascimento é obrigatória" })
  .refine((value) => /^(\d{2})\/(\d{2})\/(\d{4})$/.test(value), {
    message: "Data de nascimento inválida",
  })
  .transform((value) => {
    const [day, month, year] = value.split("/");
    return `${year}-${month}-${day}`;
  });

export const colaboradorFormSchema = z.object({
  name: z.string().min(1, { message: "Nome do colaborador é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF é obrigatório" }),
  birthdate: birthdateSchema,
  pix: z.string().min(1, { message: "Pix do colaborador é obrigatório" }),
  contact: z.string().min(1, { message: "Contato é obrigatório" }),
  email: z.string().min(1, { message: "Email é obrigatório" }),
  duty: z.enum(dutyOptions, { error: "Função do colaborador é obrigatória" }),
  address: addressFormSchema,
});

export type ColaboradorFormSchema = z.input<typeof colaboradorFormSchema>;
