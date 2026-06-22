import { useQuery, useQueryClient } from "@tanstack/react-query";
import type { ReactNode } from "react";

import { AuthContext } from "@/auth/auth-context-value";
import type { AuthRequestDTO, AuthResponseDTO, UserResponseDTO } from "@/kubb";
import { useLogin } from "@/kubb";
import {
  api,
  clearStoredAccessToken,
  getFriendlyErrorMessage,
  getStoredAccessToken,
  setStoredAccessToken
} from "@/services/api";

const AUTH_USER_QUERY_KEY = ["authUser"] as const;

function toUser(data: AuthResponseDTO): UserResponseDTO {
  return {
    username: data.username,
    role: data.role,
  };
}

export function AuthProvider({ children }: Readonly<{ children: ReactNode }>) {
  const queryClient = useQueryClient();
  const loginMutation = useLogin();
  const hasStoredToken = Boolean(getStoredAccessToken());

  const userQuery = useQuery({
    queryKey: AUTH_USER_QUERY_KEY,
    queryFn: async () => {
      const response = await api.get<AuthResponseDTO>("/v1/auth/me");
      return toUser(response.data);
    },
    enabled: hasStoredToken,
    retry: false,
  });

  const logout = () => {
    clearStoredAccessToken();
    setStoredAccessToken(null);
    queryClient.setQueryData(AUTH_USER_QUERY_KEY, null);
    queryClient.clear();
  };

  const login = async (credentials: AuthRequestDTO) => {
    try {
      const data = await loginMutation.mutateAsync({ data: credentials });
      const token = data.accessToken;

      if (!token) {
        throw new Error("Resposta de autenticação sem token");
      }

      setStoredAccessToken(token);
      setStoredAccessToken(token);
      queryClient.setQueryData(AUTH_USER_QUERY_KEY, toUser(data));
    } catch (caughtError) {
      throw new Error(getFriendlyErrorMessage(caughtError) || "Credenciais inválidas");
    }
  };

  const user = userQuery.data ?? null;
  const error = loginMutation.error ? getFriendlyErrorMessage(loginMutation.error) : null;

  return (
    <AuthContext.Provider
      value={{
        isAuthenticated: Boolean(user),
        user,
        login,
        logout,
        isLoading: userQuery.isLoading,
        isPending: loginMutation.isPending,
        error,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
