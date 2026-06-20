import { z } from "zod";

import { userRequestDTORoleEnum } from "@/kubb";

export const usuarioFormSchema = z.object({
  username: z.string().email("Informe um e-mail valido"),
  password: z.string().min(6, "A senha deve ter pelo menos 6 caracteres"),
  role: z.enum([
    userRequestDTORoleEnum.ADMIN,
    userRequestDTORoleEnum.EMPLOYEE,
    userRequestDTORoleEnum.PARENT,
    userRequestDTORoleEnum.STUDENT,
  ]),
});

export type UsuarioFormSchema = z.input<typeof usuarioFormSchema>;
