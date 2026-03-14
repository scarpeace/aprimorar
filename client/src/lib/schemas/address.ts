import z from "zod";
import { formatZip } from "../shared/formatter";

export const createAddressSchema = z.object({
    street: z.string().min(1, "Rua é obrigatória").max(100, "Rua pode ter somente até 100 caracteres"),
    number: z.string().min(1, "Número é obrigatório").max(20, "Número pode ter somente até 20 caracteres"),
    complement: z.string().max(100, "Complemento pode ter somente até 100 caracteres").optional(),
    district: z.string().min(1, "Bairro é obrigatório").max(100, "Bairro pode ter somente até 100 caracteres"),
    city: z.string().min(1, "Cidade é obrigatória").max(100, "Cidade pode ter somente até 100 caracteres"),
    state: z.string().min(1, "Estado é obrigatório").max(2, "Estado pode ter somente 2 caracteres"),
    zip: z.string().regex(/^\d{5}-?\d{3}$/, "CEP inválido"),
})

export const addressResponseSchema = z.object({
    street: z.string(),
    number: z.string(),
    complement: z.string().nullable(),
    district: z.string(),
    city: z.string(),
    state: z.string(),
    zip: z.string().transform(formatZip),
})

export type CreateStudentInput = z.infer<typeof createAddressSchema>
export type StudentResponse = z.infer<typeof addressResponseSchema>
