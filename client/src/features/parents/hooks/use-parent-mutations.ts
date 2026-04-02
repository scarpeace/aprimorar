import {
  deleteParentMutationKey,
  getParentByIdQueryKey,
  getParentsQueryKey,
  useArchiveParent,
  useDeleteParent,
  useUnarchiveParent
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useDeleteParentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useDeleteParent({
    mutation: {
      onSuccess: () => {
        toast.success("Responsável excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() })
        queryClient.invalidateQueries({ queryKey: deleteParentMutationKey() });
        navigate("/parents");
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useArchiveParentMutation() {
  const queryClient = useQueryClient();

  return useArchiveParent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() })
        queryClient.invalidateQueries({ queryKey: getParentByIdQueryKey(variables.parentId) })
      },
      onError: () => {
        toast.error("Algo deu errado ao arquivar o responsável");
      },
    },
  });
}

export function useUnarchiveParentMutation() {
  const queryClient = useQueryClient();

  return useUnarchiveParent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getParentByIdQueryKey(variables.parentId) });
      },
      onError: () => {
        toast.error("Algo deu errado ao desarquivar o responsável");
      },
    },
  })
}
