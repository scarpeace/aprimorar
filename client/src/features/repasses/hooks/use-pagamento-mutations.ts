"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useRegistrarPagamento } from "@/lib/api/generated/hooks/pagamentos/useRegistrarPagamento";
import { useCancelarPagamento } from "@/lib/api/generated/hooks/pagamentos/useCancelarPagamento";
import { getPagamentosQueryKey } from "@/lib/api/generated/hooks/pagamentos/useGetPagamentos";
import { getRepassesQueryKey } from "@/lib/api/generated/hooks/repasses/useGetRepasses";
import { getAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos/useGetAtendimentosIndividuais";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function usePagamentoMutations() {
  const queryClient = useQueryClient();

  function invalidatePagamentos() {
    return Promise.all([
      queryClient.invalidateQueries({ queryKey: getRepassesQueryKey() }),
      queryClient.invalidateQueries({ queryKey: getPagamentosQueryKey() }),
    ]);
  }

  function invalidateAtendimentos() {
    return queryClient.invalidateQueries({ queryKey: getAtendimentosIndividuaisQueryKey() });
  }

  const registerPayment = useRegistrarPagamento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao registrar o pagamento");
      },
      onSuccess: async () => {
        toast.success("Pagamento registrado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidatePagamentos()]);
      },
    },
  });

  const cancelPayment = useCancelarPagamento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao cancelar o pagamento");
      },
      onSuccess: async () => {
        toast.success("Pagamento cancelado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidatePagamentos()]);
      },
    },
  });

  return {
    registerPayment,
    cancelPayment,
  };
}
