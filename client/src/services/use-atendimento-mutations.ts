import {
  getAtendimentoByIdQueryKey,
  getAtendimentosQueryKey,
  useAgendarAtendimento,
  useExcluirAtendimento,
  useReagendarAtendimento,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/services/api.ts";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useAtendimentoMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const invalidateAtendimentos = () => {
    queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey() });
  };

  const invalidateAtendimentoDetail = (atendimentoId: number, alunoId?: string, colaboradorId?: string) => {
    queryClient.invalidateQueries({ queryKey: getAtendimentoByIdQueryKey(atendimentoId) });

    if (alunoId) {
      queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey({ alunoId }) });
    }

    if (colaboradorId) {
      queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey({ colaboradorId }) });
    }
  };

  const createAtendimento = useAgendarAtendimento({
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

  const updateAtendimento = useReagendarAtendimento({
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

  const deleteAtendimento = useExcluirAtendimento({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Atendimento excluído com sucesso");
        invalidateAtendimentos();
        invalidateAtendimentoDetail(variables.id);
        navigate("/Atendimento");
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao excluir o atendimento",
        );
      },
    },
  });

  return {
    createAtendimento,
    updateAtendimento,
    deleteAtendimento,
  };
}
