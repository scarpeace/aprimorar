"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useRegistrarPagamentoCobrancasIndividuais } from "@/lib/api/generated/hooks/cobranças individuais/useRegistrarPagamentoCobrancasIndividuais";
import { useCancelarPagamentoCobrancasIndividuais } from "@/lib/api/generated/hooks/cobranças individuais/useCancelarPagamentoCobrancasIndividuais";
import { buscarCobrancasIndividuaisQueryKey } from "@/lib/api/generated/hooks/cobranças individuais/useBuscarCobrancasIndividuais";
import { buscarLotesDeCobrancaQueryKey } from "@/lib/api/generated/hooks/cobranças individuais/useBuscarLotesDeCobranca";
import { buscarAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import { buscarCalendarioAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useCobrancaMutations() {
  const queryClient = useQueryClient();

  function invalidateCobrancas() {
    queryClient.invalidateQueries({ queryKey: buscarCobrancasIndividuaisQueryKey() });
    queryClient.invalidateQueries({
      queryKey: [buscarLotesDeCobrancaQueryKey({ alunoId: "" })[0]],
    });
  }

  function invalidateAtendimentos() {
    queryClient.invalidateQueries({ queryKey: buscarAtendimentosIndividuaisQueryKey() });
    queryClient.invalidateQueries({
      queryKey: [buscarCalendarioAtendimentosIndividuaisQueryKey({ inicio: "", fim: "" })[0]],
    });
  }

  const registerStudentPayment = useRegistrarPagamentoCobrancasIndividuais({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao registrar o pagamento do aluno");
      },
      onSuccess: async () => {
        toast.success("Pagamento do aluno registrado com sucesso");
        await Promise.all([invalidateAtendimentos(), invalidateCobrancas()]);
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
        await Promise.all([invalidateAtendimentos(), invalidateCobrancas()]);
      },
    },
  });

  return {
    registerStudentPayment,
    cancelStudentPayment,
  };
}
