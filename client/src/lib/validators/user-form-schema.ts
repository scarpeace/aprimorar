import { z } from "zod/v4";
import { userRequestDTOSchema } from "@/lib/api/generated/zod/userRequestDTOSchema";

const requiredText = (label: string) => z.string().trim().min(1, `${label} é obrigatório`);

export const userFormSchema = userRequestDTOSchema.extend({
  username: requiredText("E-mail").email("E-mail inválido"),
  password: requiredText("Senha").min(6, "Senha deve ter no mínimo 6 caracteres"),
  role: z.enum(["COLABORADOR"], { error: "Perfil é obrigatório" }),
});

export type UserFormData = z.infer<typeof userFormSchema>;
