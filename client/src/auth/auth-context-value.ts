import { createContext } from "react";

import type { AuthRequestDTO, UserResponseDTO } from "@/kubb";

export type AuthContextValue = {
  isAuthenticated: boolean;
  user: UserResponseDTO | null;
  login: (credentials: AuthRequestDTO) => Promise<void>;
  logout: () => void;
  isLoading: boolean;
  isPending: boolean;
  error: string | null;
};

export const AuthContext = createContext<AuthContextValue | null>(null);
