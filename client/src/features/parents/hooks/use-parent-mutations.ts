import { useCreateParent, type ParentResponseDTO, getParentsQueryKey, useUpdateParent, getParentByIdQueryKey, useDeleteParent, deleteParentMutationKey, useUnarchiveParent, useArchiveParent } from "@/kubb";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useCreateParentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateParent({
    mutation: {
      onError: () => {
        toast.error("Algo deu errado ao criar o responsável");
      },
      onSuccess: (createdParent: ParentResponseDTO) => {
        console.log(createdParent)
        toast.success("Responsável criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() })
        navigate(`/parents/${createdParent.parentId}`);
      },
    },
  });
}

export function useUpdateParentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useUpdateParent({
    mutation: {
      onSuccess: (updatedParent) => {
        toast.success("Responsável atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() })
        queryClient.invalidateQueries({ queryKey: getParentByIdQueryKey(updatedParent.id) })
        navigate(`/parents/${updatedParent.id}`);
      },
      onError: () => {
        toast.error("Algo deu errado ao atualizar o responsável");
      },
    },
  });
}

export function useDeleteParentMutation() {
  const queryClient = useQueryClient();

  return useDeleteParent({
    mutation: {
      onSuccess: () => {
        toast.success("Responsável excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() })
        queryClient.invalidateQueries({ queryKey: deleteParentMutationKey() });
      },
      onError: () => {
        toast.error("Algo deu errado ao excluir o responsável");
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
  });
}
