import { useLogin, useLogout, useMe, meQueryKey } from "@/kubb";
import { useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { toast } from "sonner";

const currentUserQueryKey = meQueryKey();

function isUnauthorizedError(error: unknown) {
  return axios.isAxiosError(error) && error.response?.status === 401;
}

export function useAuthSession() {
  const queryClient = useQueryClient();

  const currentUserQuery = useMe({
    query: {
      queryKey: currentUserQueryKey,
      retry: (failureCount, error) => {
        if (isUnauthorizedError(error)) {
          return false;
        }

        return failureCount < 1;
      },
      placeholderData: undefined,
    },
  });

  const refetchCurrentUser = async () => {
    await queryClient.invalidateQueries({ queryKey: currentUserQueryKey });
    return currentUserQuery.refetch();
  };

  const login = useLogin({
    mutation: {
      onSuccess: async () => {
        await refetchCurrentUser();
        toast.success("Login realizado com sucesso");
      },
    },
  });

  const logout = useLogout({
    mutation: {
      onSuccess: async () => {
        queryClient.setQueryData(currentUserQueryKey, undefined);
        queryClient.removeQueries({ queryKey: currentUserQueryKey });
        toast.success("Sessão encerrada com sucesso");
      },
    },
  });

  return {
    currentUserQuery,
    currentUser: currentUserQuery.data,
    isAuthenticated: currentUserQuery.isSuccess && Boolean(currentUserQuery.data),
    isCheckingSession: currentUserQuery.isPending,
    login,
    logout,
    refetchCurrentUser,
  };
}
