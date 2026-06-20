import {
  getAlunosByResponsavelQueryKey,
  getResponsaveisQueryKey,
  getResponsavelByIdQueryKey,
  listResponsaveisQueryKey,
  useCreateResponsavel,
  useDeleteResponsavel,
  useUpdateResponsavel,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/services/api.ts";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useResponsavelMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const invalidateResponsaveis = () => {
    queryClient.invalidateQueries({ queryKey: getResponsaveisQueryKey() });
    queryClient.invalidateQueries({ queryKey: listResponsaveisQueryKey() });
  };

  const createResponsavel = useCreateResponsavel({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao criar o responsável",
        );
      },
      onSuccess: (createdResponsavel) => {
        toast.success("Responsável criado com sucesso");
        invalidateResponsaveis();
        navigate(`/responsaveis/${createdResponsavel.id}`);
      },
    },
  });

  const updateResponsavel = useUpdateResponsavel({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável atualizado com sucesso");
        invalidateResponsaveis();
        queryClient.invalidateQueries({
          queryKey: getResponsavelByIdQueryKey(variables.responsavelId),
        });
        queryClient.invalidateQueries({
          queryKey: getAlunosByResponsavelQueryKey(variables.responsavelId),
        });
        navigate(`/responsaveis/${variables.responsavelId}`);
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar o responsável",
        );
      },
    },
  });

  const deleteResponsavel = useDeleteResponsavel({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável excluído com sucesso");
        invalidateResponsaveis();
        queryClient.invalidateQueries({
          queryKey: getResponsavelByIdQueryKey(variables.responsavelId),
        });
        queryClient.invalidateQueries({
          queryKey: getAlunosByResponsavelQueryKey(variables.responsavelId),
        });
        navigate("/responsaveis");
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao excluir o responsável",
        );
      },
    },
  });

  return {
    createResponsavel,
    updateResponsavel,
    deleteResponsavel,
  };
}
