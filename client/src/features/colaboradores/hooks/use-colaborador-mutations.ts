"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useDeactivateColaborador } from "@/lib/api/generated/hooks/colaboradores/useDeactivateColaborador";
import { useCriarColaborador } from "@/lib/api/generated/hooks/colaboradores/useCriarColaborador";

import { useActivateColaborador } from "@/lib/api/generated/hooks/colaboradores/useActivateColaborador";
import { getColaboradorByIdQueryKey } from "@/lib/api/generated/hooks/colaboradores/useGetColaboradorById";
import { getColaboradoresQueryKey } from "@/lib/api/generated/hooks/colaboradores/useGetColaboradores";
import { listColaboradoresOptionsQueryKey } from "@/lib/api/generated/hooks/colaboradores/useListColaboradoresOptions";
import { useUpdateColaborador } from "@/lib/api/generated/hooks/colaboradores/useUpdateColaborador";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useColaboradorMutations() {
  const queryClient = useQueryClient();

  function invalidateColaboradores() {
    return Promise.all([
      queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() }),
      queryClient.invalidateQueries({ queryKey: listColaboradoresOptionsQueryKey() }),
    ]);
  }

  function invalidateColaboradorDetail(colaboradorId: string) {
    return queryClient.invalidateQueries({ queryKey: getColaboradorByIdQueryKey(colaboradorId) });
  }

  const createColaborador = useCriarColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o colaborador");
      },
      onSuccess: async () => {
        toast.success("Colaborador criado com sucesso");
        await invalidateColaboradores();
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
