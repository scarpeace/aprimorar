import { userCreateRequestDTOSchema } from "@/kubb";
import { z } from "zod/v4";

export const userFormSchema = userCreateRequestDTOSchema.extend({
  employeeId: z.string().min(1, { message: "Selecione um colaborador" }),
  username: z.string().min(3, { message: "O usuário deve ter pelo menos 3 caracteres" }),
  password: z.string().min(6, { message: "A senha deve ter pelo menos 6 caracteres" }),
  role: z.enum(["STUDENT", "EMPLOYEE", "PARENT", "ADMIN"], {
    required_error: "Selecione um papel",
  }),
});

export type UserFormSchema = z.input<typeof userFormSchema>;
