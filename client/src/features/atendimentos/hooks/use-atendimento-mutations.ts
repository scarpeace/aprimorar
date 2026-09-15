"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useAgendarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos individuais/useAgendarAtendimentoIndividual";
import { useAtualizarAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos individuais/useAtualizarAtendimentoIndividual";
import { useExcluirAtendimentoIndividual } from "@/lib/api/generated/hooks/atendimentos individuais/useExcluirAtendimentoIndividual";
import { useRegistrarPagamentoCobrancasIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useRegistrarPagamentoCobrancasIndividuais";
import { useCancelarPagamentoCobrancasIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useCancelarPagamentoCobrancasIndividuais";
import { useRegistrarRepassesIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useRegistrarRepassesIndividuais";
import { useCancelarRepassesIndividuais } from "@/lib/api/generated/hooks/atendimentos individuais/useCancelarRepassesIndividuais";
import { buscarAtendimentoIndividualPorIdQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentoIndividualPorId";
import { buscarAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";

import { buscarCobrancasIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCobrancasIndividuais";
import { buscarRepassesIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarRepassesIndividuais";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useAtendimentoMutations() {
  const queryClient = useQueryClient();

  function invalidateAtendimentos() {
    queryClient.invalidateQueries({ queryKey: buscarAtendimentosIndividuaisQueryKey() });
    queryClient.invalidateQueries({ queryKey: [{ url: "/atendimentos-individuais/calendario" }] });
  }

  function invalidateFinanceiro() {
    queryClient.invalidateQueries({ queryKey: buscarCobrancasIndividuaisQueryKey() });
    queryClient.invalidateQueries({ queryKey: buscarRepassesIndividuaisQueryKey() });
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
        await Promise.all([invalidateAtendimentos(), invalidateFinanceiro(), invalidateAtendimentoDetail(createdAtendimento.id)]);
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
        await Promise.all([invalidateAtendimentos(), invalidateFinanceiro(), invalidateAtendimentoDetail(variables.id)]);
      },
    },
  });

  const deleteAtendimento = useExcluirAtendimentoIndividual({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao excluir o atendimento");
      },
      onSuccess: async (_, variables) => {
        toast.success("Atendimento excluído com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateFinanceiro(), invalidateAtendimentoDetail(variables.id)]);
      },
    },
  });

  const registerStudentPayment = useRegistrarPagamentoCobrancasIndividuais({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao registrar o pagamento do aluno");
      },
      onSuccess: async () => {
        toast.success("Pagamento do aluno registrado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateFinanceiro()]);
      },
    },
  });

  const cancelStudentPayment = useCancelarPagamentoCobrancasIndividuais({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao cancelar o pagamento do aluno");
      },
      onSuccess: async () => {
        toast.success("Pagamento do aluno cancelado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateFinanceiro()]);
      },
    },
  });

  const registerCollaboratorPayment = useRegistrarRepassesIndividuais({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao registrar o repasse do colaborador");
      },
      onSuccess: async () => {
        toast.success("Repasse do colaborador registrado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateFinanceiro()]);
      },
    },
  });

  const cancelCollaboratorPayment = useCancelarRepassesIndividuais({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao cancelar o repasse do colaborador");
      },
      onSuccess: async () => {
        toast.success("Repasse do colaborador cancelado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateFinanceiro()]);
      },
    },
  });

  return {
    createAtendimento,
    updateAtendimento,
    deleteAtendimento,
    registerStudentPayment,
    cancelStudentPayment,
    registerCollaboratorPayment,
    cancelCollaboratorPayment,
  };
}
