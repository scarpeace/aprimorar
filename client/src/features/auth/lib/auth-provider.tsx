import { useCallback, useMemo, useState } from "react";
import type { ReactNode } from "react";
import {
  AuthContext,
} from "./auth-context";
import type { AuthContextValue, AuthUser, StoredAuth } from "./auth-context";
import { useAuthMutations } from "@/features/auth/hooks/use-auth-mutations";
import { clearStoredAuth, readStoredAuth, saveStoredAuth } from "./auth-storage";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

export function AuthProvider({ children }: Readonly<{ children: ReactNode }>) {
  const [storedAuth, setStoredAuth] = useState<StoredAuth | null>(() => readStoredAuth());
  const [isPending, setIsPending] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const { loginMutation } = useAuthMutations();

  const login = useCallback(async ({ email, password }: { email: string; password: string }) => {
    setIsPending(true);
    setError(null);
    try {
      const data = await loginMutation.mutateAsync({ data: { email, password } });

      const user: AuthUser = {
        username: data.username!,
        role: data.role! as AuthUser["role"]
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
