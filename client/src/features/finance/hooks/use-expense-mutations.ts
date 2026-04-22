import {
  useCreateGeneralExpense,
  useUpdateGeneralExpense,
  useDeleteGeneralExpense,
  getGeneralExpensesQueryKey,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

interface UseExpenseMutationsProps {
  onSuccessCallback?: () => void;
}

export function useExpenseMutations({ onSuccessCallback }: UseExpenseMutationsProps = {}) {
  const queryClient = useQueryClient();

  const createExpense = useCreateGeneralExpense({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao cadastrar a despesa",
        );
      },
      onSuccess: () => {
        toast.success("Despesa cadastrada com sucesso");
        queryClient.invalidateQueries({ queryKey: getGeneralExpensesQueryKey() });
        if (onSuccessCallback) onSuccessCallback();
      },
    },
  });

  const updateExpense = useUpdateGeneralExpense({
    mutation: {
      onSuccess: () => {
        toast.success("Despesa atualizada com sucesso");
        queryClient.invalidateQueries({ queryKey: getGeneralExpensesQueryKey() });
        if (onSuccessCallback) onSuccessCallback();
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar a despesa",
        );
      },
    },
  });

  const deleteExpense = useDeleteGeneralExpense({
    mutation: {
      onSuccess: () => {
        toast.success("Despesa excluída com sucesso");
        queryClient.invalidateQueries({ queryKey: getGeneralExpensesQueryKey() });
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao excluir a despesa",
        );
      },
    },
  });

  return {
    createExpense,
    updateExpense,
    deleteExpense,
  };
}
