"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useCreateUser } from "@/lib/api/generated/hooks/user/useCreateUser";
import { useDeleteUser } from "@/lib/api/generated/hooks/user/useDeleteUser";
import { listUsersQueryKey } from "@/lib/api/generated/hooks/user/useListUsers";
import { getFriendlyErrorMessage } from "@/lib/api/client";

export function useUserMutations() {
  const queryClient = useQueryClient();

  function invalidateUsers() {
    queryClient.invalidateQueries({ queryKey: listUsersQueryKey() });
  }

  const createUser = useCreateUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o usuário");
      },
      onSuccess: async () => {
        toast.success("Usuário criado com sucesso");
        await invalidateUsers();
      },
    },
  });

  const deleteUser = useDeleteUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao excluir o usuário");
      },
      onSuccess: async () => {
        toast.success("Usuário excluído com sucesso");
        await invalidateUsers();
      },
    },
  });

  return {
    createUser,
    deleteUser,
  };
}
