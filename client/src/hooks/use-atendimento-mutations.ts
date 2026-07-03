"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useAgendarAtendimento } from "@/lib/api/generated/hooks/atendimento/useAgendarAtendimento";
import { useCancelarAtendimento } from "@/lib/api/generated/hooks/atendimento/useCancelarAtendimento";
import { useConcluirAtendimento } from "@/lib/api/generated/hooks/atendimento/useConcluirAtendimento";
import { useEfetivarPagamentoAluno } from "@/lib/api/generated/hooks/atendimento/useEfetivarPagamentoAluno";
import { useEfetivarRepasseColaborador } from "@/lib/api/generated/hooks/atendimento/useEfetivarRepasseColaborador";
import { useExcluirAtendimento } from "@/lib/api/generated/hooks/atendimento/useExcluirAtendimento";
import { getAtendimentoByIdQueryKey } from "@/lib/api/generated/hooks/atendimento/useGetAtendimentoById";
import { getAtendimentosQueryKey } from "@/lib/api/generated/hooks/atendimento/useGetAtendimentos";
import { useUpdateAtendimento } from "@/lib/api/generated/hooks/atendimento/useUpdateAtendimento";
import { getFriendlyErrorMessage } from "@/lib/api/client";

export function useAtendimentoMutations() {
  const queryClient = useQueryClient();

  function invalidateAtendimentos() {
    queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey() });
    queryClient.invalidateQueries({ queryKey: [{ url: "/v1/atendimentos/relatorio" }] });
    queryClient.invalidateQueries({ queryKey: [{ url: "/v1/atendimentos/calendario" }] });
  }

  function invalidateAtendimentoDetail(atendimentoId: number) {
    queryClient.invalidateQueries({ queryKey: getAtendimentoByIdQueryKey(atendimentoId) });
  }

  const createAtendimento = useAgendarAtendimento({
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

  const updateAtendimento = useUpdateAtendimento({
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

  const concludeAtendimento = useConcluirAtendimento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao concluir o atendimento");
      },
      onSuccess: async (_, variables) => {
        toast.success("Atendimento concluído com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.id)]);
      },
    },
  });

  const cancelAtendimento = useCancelarAtendimento({
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

  const efetivarPagamentoAluno = useEfetivarPagamentoAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao efetivar o pagamento do aluno");
      },
      onSuccess: async (_, variables) => {
        toast.success("Pagamento do aluno efetivado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.id)]);
      },
    },
  });

  const efetivarRepasseColaborador = useEfetivarRepasseColaborador({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao efetivar o repasse do colaborador");
      },
      onSuccess: async (_, variables) => {
        toast.success("Repasse do colaborador efetivado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.id)]);
      },
    },
  });

  const deleteAtendimento = useExcluirAtendimento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao excluir o atendimento");
      },
      onSuccess: async (_, variables) => {
        toast.success("Atendimento excluído com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateAtendimentoDetail(variables.id)]);
      },
    },
  });

  return {
    createAtendimento,
    updateAtendimento,
    concludeAtendimento,
    cancelAtendimento,
    efetivarPagamentoAluno,
    efetivarRepasseColaborador,
    deleteAtendimento,
  };
}
