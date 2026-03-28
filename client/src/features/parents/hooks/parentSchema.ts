import z from "zod";

export const parentInputSchema = z.object({
    name: z.string().min(1, {message: "Nome do pai é obrigatório"}),
    email: z.string().min(1, {message: "Email do pai é obrigatório"}),
    contact: z.string().min(1, {message: "Contato do pai é obrigatório"}),
    cpf: z.string().min(1, {message: "CPF do pai é obrigatório"}),
})

export const parentResponseSchema = z.object({
    name: z.string(),
    email: z.string(),
    contact: z.string(),
    cpf: z.string(),
})

export type ParentInputSchema = z.input<typeof parentInputSchema>;
export type ParentResponseSchema = z.input<typeof parentResponseSchema>;
