import type { AuthRequestDTO, UserResponseDTO } from "@/kubb";
import { createContext } from "react";
import { z } from "zod";


export const loginFormSchema = z.object({
  username: z
    .string()
    .min(1, { message: "Usuário é obrigatório" })
    .email({ message: "Usuário inválido" }),
  password: z.string().min(1, { message: "Senha é obrigatória" }),
});

export type LoginFormSchema = z.input<typeof loginFormSchema>;

export type StoredAuth = {
  token: string;
  user: UserResponseDTO;
};

export type AuthContextValue = {
  isAuthenticated: boolean;
  token: string | null;
  user: UserResponseDTO | null;
  login: (credentials: AuthRequestDTO) => Promise<void>;
  logout: () => void;
  isPending: boolean;
  error: string | null;
};

export const AUTH_STORAGE_KEY = "aprimorar.Auth";

export const AuthContext = createContext<AuthContextValue | null>(null);
