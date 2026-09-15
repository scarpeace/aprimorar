import { z } from "zod/v4";
import { loginRequestSchema } from "@/lib/api/generated/zod/loginRequestSchema";

export const loginFormSchema = loginRequestSchema.extend({
  email: z.string().trim().min(1, "E-mail é obrigatório").email("E-mail inválido"),
  password: z.string().min(1, "Senha é obrigatória"),
});

export type LoginFormData = z.infer<typeof loginFormSchema>;
