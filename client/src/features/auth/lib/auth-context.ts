import { createContext } from "react";

export type AuthRole = "EMPLOYEE" | "ADMIN";

export type AuthUser = {
  email: string;
  name: string;
  role: AuthRole;
};

export type LoginCredentials = {
  email: string;
  password: string;
};

export type StoredAuth = {
  token: string;
  user: AuthUser;
};

export type AuthContextValue = {
  isAuthenticated: boolean;
  token: string | null;
  user: AuthUser | null;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
};

export const AUTH_STORAGE_KEY = "aprimorar.auth";

export const AuthContext = createContext<AuthContextValue | null>(null);

export function readStoredAuth(): StoredAuth | null {
  const storedValue = localStorage.getItem(AUTH_STORAGE_KEY);

  if (!storedValue) {
    return null;
  }

  try {
    return JSON.parse(storedValue) as StoredAuth;
  } catch {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    return null;
  }
}

export function createMockAuth(credentials: LoginCredentials): StoredAuth {
  const normalizedEmail = credentials.email.trim().toLowerCase();
  const role: AuthRole = normalizedEmail.startsWith("admin") ? "ADMIN" : "EMPLOYEE";
  const name = normalizedEmail.split("@")[0] || "Usuario";

  return {
    token: `mock-jwt-${role.toLowerCase()}-${Date.now()}`,
    user: {
      email: normalizedEmail,
      name,
      role,
    },
  };
}
