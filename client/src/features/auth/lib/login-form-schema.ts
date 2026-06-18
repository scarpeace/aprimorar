import { z } from "zod/v4";

export const loginFormSchema = z.object({
  username: z
    .string()
    .min(1, { message: "Usuário é obrigatório" })
    .email({ message: "Usuário inválido" }),
  password: z.string().min(1, { message: "Senha é obrigatória" }),
});

export type LoginFormSchema = z.input<typeof loginFormSchema>;
