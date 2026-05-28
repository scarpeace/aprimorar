import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import {
  getDespesaByIdQueryKey,
  useCreateDespesa,
  useDeleteDespesa,
  useToggleDespesaPayment,
  useUpdateDespesa,
  getDespesasQueryKey,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

export function useDespesaMutations() {
  const queryClient = useQueryClient();

  const invalidateFinanceQueries = () => {
    queryClient.invalidateQueries({ queryKey: getDespesasQueryKey() });
    queryClient.invalidateQueries({
      queryKey: getDespesasQueryKey(),
    });
  };

  const createExpense = useCreateDespesa({
    mutation: {
      onSuccess: () => {
        toast.success("Despesa cadastrada com sucesso");
        invalidateFinanceQueries();
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const updateExpense = useUpdateDespesa({
    mutation: {
      onSuccess: (_updatedExpense, variables) => {
        toast.success("Despesa atualizada com sucesso");
        invalidateFinanceQueries();
        queryClient.invalidateQueries({
          queryKey: getDespesaByIdQueryKey(variables.id),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const deleteExpense = useDeleteDespesa({
    mutation: {
      onSuccess: () => {
        toast.success("Despesa removida com sucesso");
        invalidateFinanceQueries();
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const toggleExpensePayment = useToggleDespesaPayment({
    mutation: {
      onSuccess: (_updatedExpense, variables) => {
        toast.success("Status de pagamento da despesa atualizado");
        invalidateFinanceQueries();
        queryClient.invalidateQueries({
          queryKey: getDespesaByIdQueryKey(variables.id),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  return {
    createExpense,
    updateExpense,
    deleteExpense,
    toggleExpensePayment,
  };
}
