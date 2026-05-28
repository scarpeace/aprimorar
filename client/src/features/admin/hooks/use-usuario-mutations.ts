import { listUsersQueryKey, useCreateUser, useDeleteUser } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

export function useUsuarioMutations() {
  const queryClient = useQueryClient();

  const createUsuario = useCreateUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: () => {
        toast.success("Usuario criado com sucesso");
        queryClient.invalidateQueries({ queryKey: listUsersQueryKey() });
      },
    },
  });

  const deleteUsuario = useDeleteUser({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: () => {
        toast.success("Usuario excluido com sucesso");
        queryClient.invalidateQueries({ queryKey: listUsersQueryKey() });
      },
    },
  });

  return {
    createUsuario,
    deleteUsuario,
  };
}
