import { z } from "zod";

export const loginSchema = z.object({
  identifier: z.string().trim().min(1, "Informe seu usuário ou email"),
  password: z.string().min(1, "Informe sua senha"),
});

export type LoginSchema = z.input<typeof loginSchema>;
