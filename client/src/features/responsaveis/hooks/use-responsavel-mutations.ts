import {
    getAlunosByResponsavelQueryKey,
  getResponsaveisQueryKey,
  getResponsavelByIdQueryKey,
  useArchiveResponsavel,
  useCreateResponsavel,
  useDeleteResponsavel,
  useUnarchiveResponsavel,
  useUpdateResponsavel,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useResponsavelMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createParent = useCreateResponsavel({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao criar o responsável",
        );
      },
      onSuccess: (createdParent) => {
        toast.success("Responsável criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getResponsaveisQueryKey() });
        navigate(`/parents/${createdParent.parentId}`);
      },
    },
  });


  const updateParent = useUpdateResponsavel({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getResponsaveisQueryKey() });
        queryClient.invalidateQueries({ queryKey: getResponsavelByIdQueryKey(variables.responsavelId)});
        navigate(`/parents/${variables.responsavelId}`);
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar o responsável",
        );
      },
    },
  });


  const deleteParent = useDeleteResponsavel({
    mutation: {
      onSuccess: () => {
        toast.success("Responsável excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getResponsaveisQueryKey() });
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

  const archiveParent = useArchiveResponsavel({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getResponsaveisQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getResponsavelByIdQueryKey(variables.responsavelId),
        });
        queryClient.invalidateQueries({
          queryKey: getAlunosByResponsavelQueryKey(variables.responsavelId),
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

  const unarchiveParent = useUnarchiveResponsavel({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getResponsaveisQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getResponsavelByIdQueryKey(variables.responsavelId),
        });
        queryClient.invalidateQueries({
          queryKey: getAlunosByResponsavelQueryKey(variables.responsavelId),
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
