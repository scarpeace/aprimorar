"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useCreateDespesa } from "@/lib/api/generated/hooks/despesa/useCreateDespesa";
import { useDeleteDespesa } from "@/lib/api/generated/hooks/despesa/useDeleteDespesa";
import { getDespesaByIdQueryKey } from "@/lib/api/generated/hooks/despesa/useGetDespesaById";
import { getDespesasQueryKey } from "@/lib/api/generated/hooks/despesa/useGetDespesas";
import { useUpdateDespesa } from "@/lib/api/generated/hooks/despesa/useUpdateDespesa";
import { getFriendlyErrorMessage } from "@/lib/api/client";

export function useDespesaMutations() {
  const queryClient = useQueryClient();

  function invalidateDespesas() {
    queryClient.invalidateQueries({ queryKey: getDespesasQueryKey() });
  }

  function invalidateDespesaDetail(despesaId: number) {
    queryClient.invalidateQueries({ queryKey: getDespesaByIdQueryKey(despesaId) });
  }

  const createDespesa = useCreateDespesa({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar a despesa");
      },
      onSuccess: async (createdDespesa) => {
        toast.success("Despesa criada com sucesso");
        if (createdDespesa.id) {
          await Promise.all([invalidateDespesas(), invalidateDespesaDetail(createdDespesa.id)]);
          return;
        }
        await invalidateDespesas();
      },
    },
  });

  const updateDespesa = useUpdateDespesa({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao atualizar a despesa");
      },
      onSuccess: async (_, variables) => {
        toast.success("Despesa atualizada com sucesso");
        await Promise.all([invalidateDespesas(), invalidateDespesaDetail(variables.despesaId)]);
      },
    },
  });

  const deleteDespesa = useDeleteDespesa({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao excluir a despesa");
      },
      onSuccess: async (_, variables) => {
        toast.success("Despesa excluída com sucesso");
        await Promise.all([invalidateDespesas(), invalidateDespesaDetail(variables.despesaId)]);
      },
    },
  });

  return {
    createDespesa,
    updateDespesa,
    deleteDespesa,
  };
}
