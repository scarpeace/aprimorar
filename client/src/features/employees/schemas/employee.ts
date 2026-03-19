import { z } from "zod";
import { dutyLabels, dutyValues } from "./dutyEnum";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "../../../lib/utils/formatter";

export const employeeFormSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório").max(200, "Nome muito grande"),
  birthdate: z.string().refine((date) => {
    const d = new Date(date);
    return d < new Date();
  }, "Data de nascimento deve estar no passado"),
  pix: z.string().min(1, "Chave PIX é obrigatória"),
  contact: z
    .string()
    .regex(
      /^\(\d{2}\)\s?\d{4,5}-\d{4}$/,
      "Contato deve estar no formato (XX)XXXX-XXXX ou (XX)XXXXX-XXXX",
    ),
  cpf: z
    .string()
    .regex(
      /^\d{3}\.\d{3}\.\d{3}-\d{2}$/,
      "CPF deve estar no formato XXX.XXX.XXX-XX",
    ),
  email: z.email("E-mail inválido"),
  duty: z.enum(dutyValues),
});

export const employeeApiSchema = z.object({
  id: z.uuid(),
  name: z.string(),
  birthdate: z.string(),
  pix: z.string(),
  contact: z.string().transform(formatPhone),
  cpf: z.string().transform(formatCpf),
  email: z.email(),
  duty: z.enum(dutyValues),
  archivedAt: z.coerce.date().nullable(),
  createdAt: z.coerce.date(),
  updatedAt: z.coerce.date().nullable(),
});

export const employeeResponseSchema = employeeApiSchema.transform(
  (employee) => ({
    ...employee,
    birthdate: formatDateShortYear(employee.birthdate),
    createdAt: formatDateShortYear(employee.createdAt),
  }),
);

export const employeeOptionSchema = z.object({
  id: z.uuid(),
  name: z.string(),
});

export type EmployeeFormInput = z.infer<typeof employeeFormSchema>;
export type EmployeeApiResponse = z.infer<typeof employeeApiSchema>;
export type EmployeeResponse = z.infer<typeof employeeResponseSchema>;
export type EmployeeOption = z.infer<typeof employeeOptionSchema>;
