import { useMemo, useState } from "react";
import type { ReactNode } from "react";
import {
  AUTH_STORAGE_KEY,
  AuthContext,
  createMockAuth,
  readStoredAuth,
} from "./auth-context";
import type { AuthContextValue, StoredAuth } from "./auth-context";

export function AuthProvider({ children }: Readonly<{ children: ReactNode }>) {
  const [storedAuth, setStoredAuth] = useState<StoredAuth | null>(() => readStoredAuth());

  const value = useMemo<AuthContextValue>(() => ({
    isAuthenticated: Boolean(storedAuth?.token),
    token: storedAuth?.token ?? null,
    user: storedAuth?.user ?? null,
    async login(credentials) {
      const nextAuth = createMockAuth(credentials);

      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(nextAuth));
      setStoredAuth(nextAuth);
    },
    logout() {
      localStorage.removeItem(AUTH_STORAGE_KEY);
      setStoredAuth(null);
    },
  }), [storedAuth]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}
