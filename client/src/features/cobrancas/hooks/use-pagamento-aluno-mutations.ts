"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useRegistrarPagamentoAluno } from "@/lib/api/generated/hooks/cobranças de alunos/useRegistrarPagamentoAluno";
import { useCancelarPagamentoAluno } from "@/lib/api/generated/hooks/cobranças de alunos/useCancelarPagamentoAluno";
import { buscarCobrancasAlunosQueryKey } from "@/lib/api/generated/hooks/cobranças de alunos/useBuscarCobrancasAlunos";
import { buscarLotesDeCobrancaQueryKey } from "@/lib/api/generated/hooks/cobranças de alunos/useBuscarLotesDeCobranca";
import { buscarAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarAtendimentosIndividuais";
import { buscarCalendarioAtendimentosIndividuaisQueryKey } from "@/lib/api/generated/hooks/atendimentos individuais/useBuscarCalendarioAtendimentosIndividuais";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function usePagamentoAlunoMutations() {
  const queryClient = useQueryClient();

  function invalidateCobrancas() {
    queryClient.invalidateQueries({ queryKey: buscarCobrancasAlunosQueryKey() });
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

  const registerStudentPayment = useRegistrarPagamentoAluno({
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

  const cancelStudentPayment = useCancelarPagamentoAluno({
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
