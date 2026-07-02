"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useCreateResponsavel } from "@/lib/api/generated/hooks/responsavel/useCreateResponsavel";
import { useDeleteResponsavel } from "@/lib/api/generated/hooks/responsavel/useDeleteResponsavel";
import { useUpdateResponsavel } from "@/lib/api/generated/hooks/responsavel/useUpdateResponsavel";
import { getResponsaveisQueryKey } from "@/lib/api/generated/hooks/responsavel/useGetResponsaveis";
import { getResponsavelByIdQueryKey } from "@/lib/api/generated/hooks/responsavel/useGetResponsavelById";
import { listResponsaveisQueryKey } from "@/lib/api/generated/hooks/responsavel/useListResponsaveis";
import { getFriendlyErrorMessage } from "@/lib/api/client";

export function useResponsavelMutations() {
  const queryClient = useQueryClient();

  function invalidateResponsaveis() {
    queryClient.invalidateQueries({ queryKey: getResponsaveisQueryKey() });
    queryClient.invalidateQueries({ queryKey: listResponsaveisQueryKey() });
  }

  function invalidateResponsavelDetail(responsavelId: string) {
    queryClient.invalidateQueries({ queryKey: getResponsavelByIdQueryKey(responsavelId) });
  }

  const createResponsavel = useCreateResponsavel({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o responsável");
      },
      onSuccess: async (createdResponsavel) => {
        toast.success("Responsável criado com sucesso");
        await Promise.all([invalidateResponsaveis(), invalidateResponsavelDetail(createdResponsavel.id)]);
      },
    },
  });

  const updateResponsavel = useUpdateResponsavel({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao atualizar o responsável");
      },
      onSuccess: async (_, variables) => {
        toast.success("Responsável atualizado com sucesso");
        await Promise.all([invalidateResponsaveis(), invalidateResponsavelDetail(variables.responsavelId)]);
      },
    },
  });

  const deleteResponsavel = useDeleteResponsavel({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao excluir o responsável");
      },
      onSuccess: async (_, variables) => {
        toast.success("Responsável excluído com sucesso");
        await Promise.all([invalidateResponsaveis(), invalidateResponsavelDetail(variables.responsavelId)]);
      },
    },
  });

  return {
    createResponsavel,
    updateResponsavel,
    deleteResponsavel,
  };
}
