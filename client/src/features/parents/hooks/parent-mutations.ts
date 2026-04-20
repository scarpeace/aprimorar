import {
  deleteParentMutationKey,
  getParentByIdQueryKey,
  getParentsQueryKey,
  getStudentsByParentQueryKey,
  useArchiveParent,
  useCreateParent,
  useDeleteParent,
  useUnarchiveParent,
  useUpdateParent,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useParentMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createParent = useCreateParent({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao criar o responsável",
        );
      },
      onSuccess: (createdParent) => {
        toast.success("Responsável criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() });
        navigate(`/parents/${createdParent.parentId}`);
      },
    },
  });


  const updateParent = useUpdateParent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getParentByIdQueryKey(variables.parentId),
        });
        navigate(`/parents/${variables.parentId}`);
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar o responsável",
        );
      },
    },
  });


  const deleteParent = useDeleteParent({
    mutation: {
      onSuccess: () => {
        toast.success("Responsável excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: deleteParentMutationKey() });
        navigate("/parents");
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao excluir o responsável",
        );
      },
    },
  });

  const archiveParent = useArchiveParent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getParentByIdQueryKey(variables.parentId),
        });
        queryClient.invalidateQueries({
          queryKey: getStudentsByParentQueryKey(variables.parentId),
        });
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao arquivar o responsável",
        );
      },
    },
  });

  const unarchiveParent = useUnarchiveParent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getParentByIdQueryKey(variables.parentId),
        });
        queryClient.invalidateQueries({
          queryKey: getStudentsByParentQueryKey(variables.parentId),
        });
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao desarquivar o responsável",
        );
      },
    },
  });

  return {
    createParent,
    updateParent,
    deleteParent,
    archiveParent,
    unarchiveParent,
  };
}
