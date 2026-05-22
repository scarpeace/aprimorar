import { useCallback, useMemo, useState } from "react";
import type { ReactNode } from "react";
import { publicApi } from "@/lib/shared/api";
import {
  AUTH_STORAGE_KEY,
  AuthContext,
  readStoredAuth,
} from "./auth-context";
import type { AuthContextValue, AuthUser, StoredAuth } from "./auth-context";
import type { AuthResponseDTO } from "@/kubb/types/AuthResponseDTO";

export function AuthProvider({ children }: Readonly<{ children: ReactNode }>) {
  const [storedAuth, setStoredAuth] = useState<StoredAuth | null>(() => readStoredAuth());
  const [isPending, setIsPending] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const login = useCallback(async ({ email, password }: { email: string; password: string }) => {
    setIsPending(true);
    setError(null);
    try {
      const { data } = await publicApi.post<AuthResponseDTO>("/v1/auth/login", { email, password });

      const user: AuthUser = {
        username: data.username!,
        role: data.role! as AuthUser["role"]
      };

      const nextAuth: StoredAuth = { token: data.accessToken!, user };

      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(nextAuth));
      setStoredAuth(nextAuth);
    } catch {
      const message = "Credenciais inválidas";
      setError(message);
      throw new Error(message);
    } finally {
      setIsPending(false);
    }
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem(AUTH_STORAGE_KEY);
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
