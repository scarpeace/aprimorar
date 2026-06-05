import type { ResponsavelRequestDTO } from "@/kubb";
import { z } from "zod/v4";

const birthdateSchema = z.string()
  .min(1, { message: "Data de nascimento é obrigatória" })
  .refine((value) => /^(\d{2})\/(\d{2})\/(\d{4})$/.test(value), {
    message: "Data de nascimento inválida",
  })
  .transform((value) => {
    const [day, month, year] = value.split("/");
    return `${year}-${month}-${day}`;
  });

export const responsavelFormSchema: z.ZodType<ResponsavelRequestDTO> = z.object({
  name: z.string().min(1, { message: "Nome do responsável é obrigatório" }),
  email: z.string().min(1, { message: "Email do responsável é obrigatório" }),
  contact: z.string().min(1, { message: "Contato do responsável é obrigatório" }),
  birthdate: birthdateSchema,
  pix: z.string().min(1, { message: "Pix do responsável é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF do responsável é obrigatório" }),
});

export type ResponsavelFormSchema = z.input<typeof responsavelFormSchema>;
