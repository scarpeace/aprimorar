import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getAppointmentFinanceReportQueryKey,
  getExpenseByIdQueryKey,
  getExpensesQueryKey,
  useCreateExpense,
  useDeleteExpense,
  useUpdateExpense,
} from "@/kubb";

export function useExpenseMutations() {
  const queryClient = useQueryClient();

  const invalidateFinanceQueries = () => {
    queryClient.invalidateQueries({ queryKey: getExpensesQueryKey() });
    queryClient.invalidateQueries({
      queryKey: getAppointmentFinanceReportQueryKey(),
    });
  };

  const createExpense = useCreateExpense({
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

  const updateExpense = useUpdateExpense({
    mutation: {
      onSuccess: (_updatedExpense, variables) => {
        toast.success("Despesa atualizada com sucesso");
        invalidateFinanceQueries();
        queryClient.invalidateQueries({
          queryKey: getExpenseByIdQueryKey(variables.id),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const deleteExpense = useDeleteExpense({
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

  return {
    createExpense,
    updateExpense,
    deleteExpense,
  };
}
