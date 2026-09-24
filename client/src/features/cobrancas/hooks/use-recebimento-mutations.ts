"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useRegistrarRecebimento } from "@/lib/api/generated/hooks/recebimentos/useRegistrarRecebimento";
import { useCancelarRecebimento } from "@/lib/api/generated/hooks/recebimentos/useCancelarRecebimento";
import { getCobrancasQueryKey } from "@/lib/api/generated/hooks/cobrancas/useGetCobrancas";
import { getRecebimentosQueryKey } from "@/lib/api/generated/hooks/recebimentos/useGetRecebimentos";
import { getAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos/useGetAtendimentosIndividuais";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useRecebimentoMutations() {
  const queryClient = useQueryClient();

  function invalidateFinanceiro() {
    return Promise.all([
      queryClient.invalidateQueries({ queryKey: getCobrancasQueryKey() }),
      queryClient.invalidateQueries({ queryKey: getRecebimentosQueryKey() }),
      queryClient.invalidateQueries({ queryKey: getAtendimentosIndividuaisQueryKey() }),
    ]);
  }

  const registerRecebimento = useRegistrarRecebimento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao registrar o recebimento");
      },
      onSuccess: async () => {
        toast.success("Recebimento registrado com sucesso");
        await invalidateFinanceiro();
      },
    },
  });

  const cancelRecebimento = useCancelarRecebimento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao cancelar o recebimento");
      },
      onSuccess: async () => {
        toast.success("Recebimento cancelado com sucesso");
        await invalidateFinanceiro();
      },
    },
  });

  return {
    registerRecebimento,
    cancelRecebimento,
  };
}
