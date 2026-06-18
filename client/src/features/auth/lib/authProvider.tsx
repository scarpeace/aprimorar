import { useCallback, useMemo, useState } from "react";
import type { ReactNode } from "react";
import {
  AuthContext,
} from "./authContext";
import type { AuthContextValue, StoredAuth } from "./authContext";
import { useAuthMutations } from "@/features/auth/hooks/use-auth-mutations";
import { clearStoredAuth, readStoredAuth, saveStoredAuth } from "./auth-storage";
import { getFriendlyErrorMessage } from "@/lib/shared/api/api";
import type { AuthRequestDTO, UserResponseDTO } from "@/kubb";

export function AuthProvider({ children }: Readonly<{ children: ReactNode }>) {
  const [storedAuth, setStoredAuth] = useState<StoredAuth | null>(() => readStoredAuth());
  const [isPending, setIsPending] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { loginMutation } = useAuthMutations();

  const login = useCallback(async ({ username, password }: AuthRequestDTO) => {
    setIsPending(true);
    setError(null);
    try {
      const data = await loginMutation.mutateAsync({ data: { username, password } });

      const user: UserResponseDTO = {
        username: data.username!,
        role: data.role! as UserResponseDTO["role"]
      };

      const nextAuth: StoredAuth = { token: data.accessToken!, user };

      saveStoredAuth(nextAuth);
      setStoredAuth(nextAuth);
    } catch (caughtError) {
      const message = getFriendlyErrorMessage(caughtError) || "Credenciais inválidas";
      setError(message);
      throw new Error(message);
    } finally {
      setIsPending(false);
    }
  }, [loginMutation]);

  const logout = useCallback(() => {
    clearStoredAuth();
    setStoredAuth(null);
    setError(null);
  }, []);

  const value = useMemo<AuthContextValue>(() => ({
    isAuthenticated: Boolean(storedAuth?.token),
    token: storedAuth?.token ?? null,
    user: storedAuth?.user ?? null,
    login,
    logout,
    isPending,
    error,
  }), [storedAuth, login, logout, isPending, error]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
