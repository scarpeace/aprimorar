"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useActivateUser } from "@/lib/api/generated/hooks/usuário/useActivateUser";
import { useCreateUser } from "@/lib/api/generated/hooks/usuário/useCreateUser";
import { useDeactivateUser } from "@/lib/api/generated/hooks/usuário/useDeactivateUser";
import { useDeleteUser } from "@/lib/api/generated/hooks/usuário/useDeleteUser";
import { getUserByIdQueryKey } from "@/lib/api/generated/hooks/usuário/useGetUserById";
import { getUsersQueryKey } from "@/lib/api/generated/hooks/usuário/useGetUsers";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useUserMutations() {
  const queryClient = useQueryClient();

  function invalidateUsers() {
    queryClient.invalidateQueries({ queryKey: getUsersQueryKey() });
  }

  function invalidateUserDetail(userId: string) {
    queryClient.invalidateQueries({ queryKey: getUserByIdQueryKey(userId) });
  }

  const createUser = useCreateUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o usuário");
      },
      onSuccess: async (createdUser) => {
        toast.success("Usuário criado com sucesso");
        await Promise.all([invalidateUsers(), invalidateUserDetail(createdUser.id)]);
      },
    },
  });

  const activateUser = useActivateUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao ativar o usuário");
      },
      onSuccess: async (_, variables) => {
        toast.success("Usuário ativado com sucesso");
        await Promise.all([invalidateUsers(), invalidateUserDetail(variables.userId)]);
      },
    },
  });

  const deactivateUser = useDeactivateUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao desativar o usuário");
      },
      onSuccess: async (_, variables) => {
        toast.success("Usuário desativado com sucesso");
        await Promise.all([invalidateUsers(), invalidateUserDetail(variables.userId)]);
      },
    },
  });

  const deleteUser = useDeleteUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao excluir o usuário");
      },
      onSuccess: async (_, variables) => {
        toast.success("Usuário excluído com sucesso");
        await Promise.all([invalidateUsers(), invalidateUserDetail(variables.userId)]);
      },
    },
  });

  return {
    createUser,
    activateUser,
    deactivateUser,
    deleteUser,
  };
}
