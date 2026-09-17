"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useRegistrarRepassesIndividuais } from "@/lib/api/generated/hooks/repasses individuais/useRegistrarRepassesIndividuais";
import { useCancelarRepassesIndividuais } from "@/lib/api/generated/hooks/repasses individuais/useCancelarRepassesIndividuais";
import { buscarRepassesIndividuaisQueryKey } from "@/lib/api/generated/hooks/repasses individuais/useBuscarRepassesIndividuais";
import { buscarLotesDeRepasseQueryKey } from "@/lib/api/generated/hooks/repasses individuais/useBuscarLotesDeRepasse";
import { buscarAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import { buscarCalendarioAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useRepasseMutations() {
  const queryClient = useQueryClient();

  function invalidateRepasses() {
    queryClient.invalidateQueries({ queryKey: buscarRepassesIndividuaisQueryKey() });
    queryClient.invalidateQueries({
      queryKey: [buscarLotesDeRepasseQueryKey({ colaboradorId: "" })[0]],
    });
  }

  function invalidateAtendimentos() {
    queryClient.invalidateQueries({ queryKey: buscarAtendimentosIndividuaisQueryKey() });
    queryClient.invalidateQueries({
      queryKey: [buscarCalendarioAtendimentosIndividuaisQueryKey({ inicio: "", fim: "" })[0]],
    });
  }

  const registerCollaboratorPayment = useRegistrarRepassesIndividuais({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao registrar o repasse do colaborador");
      },
      onSuccess: async () => {
        toast.success("Repasse do colaborador registrado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateRepasses()]);
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
        await Promise.all([invalidateAtendimentos(), invalidateRepasses()]);
      },
    },
  });

  return {
    registerCollaboratorPayment,
    cancelCollaboratorPayment,
  };
}
