"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useDeactivateColaborador } from "@/lib/api/generated/hooks/colaborador/useDeactivateColaborador";
import { useCreateColaborador } from "@/lib/api/generated/hooks/colaborador/useCreateColaborador";

import { useActivateColaborador } from "@/lib/api/generated/hooks/colaborador/useActivateColaborador";
import { findColaboradorByIdQueryKey } from "@/lib/api/generated/hooks/colaborador/useFindColaboradorById";
import { getColaboradoresQueryKey } from "@/lib/api/generated/hooks/colaborador/useGetColaboradores";
import { getColaboradoresListQueryKey } from "@/lib/api/generated/hooks/colaborador/useGetColaboradoresList";
import { useUpdateColaborador } from "@/lib/api/generated/hooks/colaborador/useUpdateColaborador";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useColaboradorMutations() {
  const queryClient = useQueryClient();

  function invalidateColaboradores() {
    queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
    queryClient.invalidateQueries({ queryKey: getColaboradoresListQueryKey() });
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

  const deactivateColaborador = useDeactivateColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao desativar o colaborador");
      },
      onSuccess: async (_, variables) => {
        toast.success("Colaborador desativado com sucesso");
        await Promise.all([invalidateColaboradores(), invalidateColaboradorDetail(variables.colaboradorId)]);
      },
    },
  });

  const activateColaborador = useActivateColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao ativar o colaborador");
      },
      onSuccess: async (_, variables) => {
        toast.success("Colaborador ativado com sucesso");
        await Promise.all([invalidateColaboradores(), invalidateColaboradorDetail(variables.colaboradorId)]);
      },
    },
  });


  return {
    createColaborador,
    activateColaborador,
    deactivateColaborador,
    updateColaborador,
  };
}
