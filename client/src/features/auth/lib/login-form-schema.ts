import type { AuthRequestDTO } from "@/kubb";
import { z } from "zod/v4";

export const loginFormSchema: z.ZodType<AuthRequestDTO> = z.object({
  email: z
    .string()
    .min(1, { message: "Email é obrigatório" })
    .email({ message: "Email inválido" }),
  password: z.string().min(1, { message: "Senha é obrigatória" }),
});

export type LoginFormSchema = z.input<typeof loginFormSchema>;
