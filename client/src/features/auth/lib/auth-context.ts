import { createContext } from "react";

export type AuthRole = "ADMIN" | "EMPLOYEE";

export type AuthUser = {
  username: string;
  role: AuthRole;
};

export type StoredAuth = {
  token: string;
  user: AuthUser;
};

export type AuthContextValue = {
  isAuthenticated: boolean;
  token: string | null;
  user: AuthUser | null;
  login: (credentials: { email: string; password: string }) => Promise<void>;
  logout: () => void;
  isPending: boolean;
  error: string | null;
};

export const AUTH_STORAGE_KEY = "aprimorar.auth";

export const AuthContext = createContext<AuthContextValue | null>(null);
