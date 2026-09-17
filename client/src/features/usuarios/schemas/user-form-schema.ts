import { z } from "zod/v4";
import { userCreateRequestSchema } from "@/lib/api/generated/zod/userCreateRequestSchema";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);

export const userFormSchema = userCreateRequestSchema.extend({
  email: requiredText("E-mail").email("E-mail inválido"),
  password: requiredText("Senha").min(6, "Senha deve ter no mínimo 6 caracteres"),
  role: z.enum(["SECRETARIA"], { error: "Perfil é obrigatório" }),
});

export type UserFormData = z.infer<typeof userFormSchema>;
