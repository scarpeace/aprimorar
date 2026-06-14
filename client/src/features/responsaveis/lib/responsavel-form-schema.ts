import { z } from "zod/v4";

const dataNascimentoSchema = z.string()
  .refine((value) => /^(\d{2})\/(\d{2})\/(\d{4})$/.test(value), {
    message: "Data de nascimento inválida",
  })
  .transform((value) => {
    const [day, month, year] = value.split("/");
    return `${year}-${month}-${day}`;
  });

export const responsavelFormSchema = z.object({
  nome: z.string().min(1, { message: "Nome do responsável é obrigatório" }),
  email: z.string().min(1, { message: "Email do responsável é obrigatório" }),
  telefone: z.string().min(1, { message: "Contato do responsável é obrigatório" }),
  dataNascimento: z.literal("").transform(() => undefined).or(dataNascimentoSchema).optional(),
  cpf: z.string().min(1, { message: "CPF do responsável é obrigatório" }),
});

export type ResponsavelFormSchema = z.input<typeof responsavelFormSchema>;
