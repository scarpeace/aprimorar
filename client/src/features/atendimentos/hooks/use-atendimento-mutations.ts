"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useAgendarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos individuais/useAgendarAtendimentoIndividual";
import { useAtualizarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos individuais/useAtualizarAtendimentoIndividual";
import { useCancelarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos individuais/useCancelarAtendimentoIndividual";
import { useRealizarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos individuais/useRealizarAtendimentoIndividual";
import { buscarAtendimentoIndividualPorIdQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentoIndividualPorId";
import { buscarAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import { buscarCalendarioAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";

import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useAtendimentoMutations() {
  const queryClient = useQueryClient();

  function invalidateAtendimentos() {
    queryClient.invalidateQueries({ queryKey: buscarAtendimentosIndividuaisQueryKey() });
    queryClient.invalidateQueries({
      queryKey: [
        buscarCalendarioAtendimentosIndividuaisQueryKey({ inicio: "", fim: "" })[0],
      ],
    });
  }


  function invalidateAtendimentoDetail(atendimentoId: number) {
    queryClient.invalidateQueries({ queryKey: buscarAtendimentoIndividualPorIdQueryKey(atendimentoId) });
  }

  const createAtendimento = useAgendarAtendimentoIndividual({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o atendimento");
      },
      onSuccess: async (createdAtendimento) => {
        toast.success("Atendimento criado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(createdAtendimento.id)]);
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
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.id)]);
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
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.id)]);
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
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.id)]);
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
