"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useArquivarColaborador } from "@/lib/api/generated/hooks/colaborador/useArquivarColaborador";
import { useCreateColaborador } from "@/lib/api/generated/hooks/colaborador/useCreateColaborador";
import { useDesarquivarColaborador } from "@/lib/api/generated/hooks/colaborador/useDesarquivarColaborador";
import { findColaboradorByIdQueryKey } from "@/lib/api/generated/hooks/colaborador/useFindColaboradorById";
import { getColaboradoresQueryKey } from "@/lib/api/generated/hooks/colaborador/useGetColaboradores";
import { useUpdateColaborador } from "@/lib/api/generated/hooks/colaborador/useUpdateColaborador";
import { getFriendlyErrorMessage } from "@/lib/api/client";

export function useColaboradorMutations() {
  const queryClient = useQueryClient();

  function invalidateColaboradores() {
    queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
  }

  function invalidateColaboradorDetail(colaboradorId: string) {
    queryClient.invalidateQueries({ queryKey: findColaboradorByIdQueryKey(colaboradorId) });
  }

  const createColaborador = useCreateColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o colaborador");
      },
      onSuccess: async (createdColaborador) => {
        toast.success("Colaborador criado com sucesso");
        await Promise.all([invalidateColaboradores(), invalidateColaboradorDetail(createdColaborador.id)]);
      },
    },
  });

  const updateColaborador = useUpdateColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao atualizar o colaborador");
      },
      onSuccess: async (_, variables) => {
        toast.success("Colaborador atualizado com sucesso");
        await Promise.all([invalidateColaboradores(), invalidateColaboradorDetail(variables.colaboradorId)]);
      },
    },
  });

  const archiveColaborador = useArquivarColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao arquivar o colaborador");
      },
      onSuccess: async (_, variables) => {
        toast.success("Colaborador arquivado com sucesso");
        await Promise.all([invalidateColaboradores(), invalidateColaboradorDetail(variables.colaboradorId)]);
      },
    },
  });

  const unarchiveColaborador = useDesarquivarColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao desarquivar o colaborador");
      },
      onSuccess: async (_, variables) => {
        toast.success("Colaborador desarquivado com sucesso");
        await Promise.all([invalidateColaboradores(), invalidateColaboradorDetail(variables.colaboradorId)]);
      },
    },
  });

  return {
    createColaborador,
    archiveColaborador,
    unarchiveColaborador,
    updateColaborador,
  };
}
