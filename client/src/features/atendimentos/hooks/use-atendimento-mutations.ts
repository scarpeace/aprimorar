import {
  getAtendimentoByIdQueryKey,
  getAtendimentosByAlunoQueryKey,
  getAtendimentosByColaboradorQueryKey,
  getAtendimentosQueryKey,
  getIndicadoresAtendimentosQueryKey,
  useAlternarCobrancaAlunoAtendimento,
  useAlternarPagamentoColaboradorAtendimento,
  useCreateAtendimento,
  useDeleteAtendimento,
  useUpdateAtendimento,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useAtendimentoMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const invalidateAtendimentos = () => {
    queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey() });
    queryClient.invalidateQueries({ queryKey: getIndicadoresAtendimentosQueryKey() });
  };

  const invalidateAtendimentoDetail = (atendimentoId: string, alunoId?: string, colaboradorId?: string) => {
    queryClient.invalidateQueries({ queryKey: getAtendimentoByIdQueryKey(atendimentoId) });

    if (alunoId) {
      queryClient.invalidateQueries({ queryKey: getAtendimentosByAlunoQueryKey(alunoId) });
    }

    if (colaboradorId) {
      queryClient.invalidateQueries({ queryKey: getAtendimentosByColaboradorQueryKey(colaboradorId) });
    }
  };

  const createAtendimento = useCreateAtendimento({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao criar o atendimento",
        );
      },
      onSuccess: (createdAtendimento) => {
        toast.success("Atendimento criado com sucesso");
        invalidateAtendimentos();
        invalidateAtendimentoDetail(
          createdAtendimento.id,
          createdAtendimento.alunoId,
          createdAtendimento.colaboradorId,
        );
        navigate(`/atendimentos/${createdAtendimento.id}`);
      },
    },
  });

  const updateAtendimento = useUpdateAtendimento({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar o atendimento",
        );
      },
      onSuccess: (updatedAtendimento, variables) => {
        toast.success("Atendimento atualizado com sucesso");
        invalidateAtendimentos();
        invalidateAtendimentoDetail(
          variables.id,
          updatedAtendimento.alunoId,
          updatedAtendimento.colaboradorId,
        );
        navigate(`/atendimentos/${updatedAtendimento.id}`);
      },
    },
  });

  const deleteAtendimento = useDeleteAtendimento({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Atendimento excluído com sucesso");
        invalidateAtendimentos();
        invalidateAtendimentoDetail(variables.id);
        navigate("/atendimentos");
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao excluir o atendimento",
        );
      },
    },
  });

  const alternarCobrancaAluno = useAlternarCobrancaAlunoAtendimento({
    mutation: {
      onSuccess: (updatedAtendimento, variables) => {
        toast.success("Status da cobrança atualizado");
        invalidateAtendimentos();
        invalidateAtendimentoDetail(
          variables.id,
          updatedAtendimento.alunoId,
          updatedAtendimento.colaboradorId,
        );
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar a cobrança",
        );
      },
    },
  });

  const alternarPagamentoColaborador = useAlternarPagamentoColaboradorAtendimento({
    mutation: {
      onSuccess: (updatedAtendimento, variables) => {
        toast.success("Status do pagamento atualizado");
        invalidateAtendimentos();
        invalidateAtendimentoDetail(
          variables.id,
          updatedAtendimento.alunoId,
          updatedAtendimento.colaboradorId,
        );
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar o pagamento",
        );
      },
    },
  });

  return {
    createAtendimento,
    updateAtendimento,
    deleteAtendimento,
    alternarCobrancaAluno,
    alternarPagamentoColaborador,
  };
}
