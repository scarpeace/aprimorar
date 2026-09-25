"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useAgendarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos/useAgendarAtendimentoIndividual";
import { useAtualizarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos/useAtualizarAtendimentoIndividual";
import { useCancelarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos/useCancelarAtendimentoIndividual";
import { useRealizarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos/useRealizarAtendimentoIndividual";
import { getAtendimentoIndividualByIdQueryKey } from "@/lib/api/generated/hooks/atendimentos/useGetAtendimentoIndividualById";
import { getAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos/useGetAtendimentosIndividuais";

import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useAtendimentoMutations() {
  const queryClient = useQueryClient();

  function invalidateAtendimentos() {
    return queryClient.invalidateQueries({ queryKey: getAtendimentosIndividuaisQueryKey() });
  }


  function invalidateAtendimentoDetail(atendimentoId: number) {
    return queryClient.invalidateQueries({ queryKey: getAtendimentoIndividualByIdQueryKey(atendimentoId) });
  }

  const createAtendimento = useAgendarAtendimentoIndividual({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o atendimento");
      },
      onSuccess: async () => {
        toast.success("Atendimento criado com sucesso");
        await invalidateAtendimentos();
      },
    },
  });

  const updateAtendimento = useAtualizarAtendimentoIndividual({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao atualizar o atendimento");
      },
      onSuccess: async (_, variables) => {
        toast.success("Atendimento atualizado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.atendimentoId)]);
      },
    },
  });

  const realizeAtendimento = useRealizarAtendimentoIndividual({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao realizar o atendimento");
      },
      onSuccess: async (_, variables) => {
        toast.success("Atendimento realizado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.atendimentoId)]);
      },
    },
  });

  const cancelAtendimento = useCancelarAtendimentoIndividual({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao cancelar o atendimento");
      },
      onSuccess: async (_, variables) => {
        toast.success("Atendimento cancelado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.atendimentoId)]);
      },
    },
  });


  return {
    createAtendimento,
    updateAtendimento,
    realizeAtendimento,
    cancelAtendimento,
  };
}
