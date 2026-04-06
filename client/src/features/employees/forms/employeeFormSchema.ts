import { employeeRequestDTOSchema } from "@/kubb";
import { z } from "zod/v4";

export const employeeFormSchema = employeeRequestDTOSchema.extend({
  name: z.string().min(1, { message: "Nome do aluno é obrigatório" }),
  cpf: z.string().min(1, { message: "CPF é obrigatório" }),
  birthdate: z.string().min(1, { message: "Data de nascimento é obrigatória" }),
  contact: z.string().min(1, { message: "Contato é obrigatório" }),
  email: z.string().min(1, { message: "Email é obrigatório" }),
});

export type EmployeeFormSchema = z.input<typeof employeeFormSchema>;
