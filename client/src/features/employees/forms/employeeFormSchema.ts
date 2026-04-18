import { employeeRequestDTOSchema } from "@/kubb";
import { z } from "zod/v4";

export const employeeFormSchema = employeeRequestDTOSchema.extend({
  name: z.string().min(1, { message: "Nome do aluno é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF é obrigatório" }),
    birthdate: z.string().min(1, { message: "Data de nascimento é obrigatória" })
      .refine((value) => /^(\d{2})\/(\d{2})\/(\d{4})$/.test(value), {
    message: "Data de nascimento inválida",
  })
  .transform((value) => {
    const [day, month, year] = value.split("/");
    return `${year}-${month}-${day}`;
  }),
  contact: z.string().min(1, { message: "Contato é obrigatório" }),
  email: z.string().min(1, { message: "Email é obrigatório" }),
});

export type EmployeeFormSchema = z.input<typeof employeeFormSchema>;
