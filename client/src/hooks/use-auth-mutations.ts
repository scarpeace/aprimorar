"use client";

import { publicClient } from "@/lib/api/kubb-client";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useLogin } from "@/lib/api/generated/hooks/autenticação/useLogin";
import { useLogout } from "@/lib/api/generated/hooks/autenticação/useLogout";
import { loginResponseSchema } from "@/lib/api/generated/zod/loginResponseSchema";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";
import {
  clearAccessToken,
  setAccessToken,
} from "@/lib/auth/token-store";

export function useAuthMutations() {
  const queryClient = useQueryClient();

  const login = useLogin({
    client: {
      client: publicClient,
    },
    mutation: {
      onSuccess: (data) => {
        const auth = loginResponseSchema.parse(data);
        setAccessToken(auth.accessToken);
        toast.success("Login realizado com sucesso");
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Não foi possível fazer login.");
      },
    },
  });

  const logout = useLogout({
    client: {
      client: publicClient,
    },
    mutation: {
      onSuccess: () => {
        clearAccessToken();
        queryClient.clear();
        toast.success("Logout realizado com sucesso");
      },
      onError: (error) => {
        clearAccessToken();
        queryClient.clear();
        toast.error(getFriendlyErrorMessage(error) || "Não foi possível fazer logout.");
      },
    },
  });

  return {
    login,
    logout,
  };
}
