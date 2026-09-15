"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useActivateAluno } from "@/lib/api/generated/hooks/aluno/useActivateAluno";
import { useCriarAluno } from "@/lib/api/generated/hooks/aluno/useCriarAluno";

import { useDeactivateAluno } from "@/lib/api/generated/hooks/aluno/useDeactivateAluno";
import { useUpdateAluno } from "@/lib/api/generated/hooks/aluno/useUpdateAluno";
import { getAlunoByIdQueryKey } from "@/lib/api/generated/hooks/aluno/useGetAlunoById";
import { getAlunosQueryKey } from "@/lib/api/generated/hooks/aluno/useGetAlunos";
import { listAlunosQueryKey } from "@/lib/api/generated/hooks/aluno/useListAlunos";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useAlunoMutations() {
  const queryClient = useQueryClient();

  function invalidateAlunos() {
    queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
    queryClient.invalidateQueries({ queryKey: listAlunosQueryKey() });
  }

  function invalidateAlunoDetail(alunoId: string) {
    queryClient.invalidateQueries({ queryKey: getAlunoByIdQueryKey(alunoId) });
  }

  const createAluno = useCriarAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao criar o aluno");
      },
      onSuccess: async (createdAluno) => {
        toast.success("Aluno criado com sucesso");
        await Promise.all([invalidateAlunos(), invalidateAlunoDetail(createdAluno.id)]);
      },
    },
  });

  const deactivateAluno = useDeactivateAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao desativar o aluno");
      },
      onSuccess: async (_, variables) => {
        toast.success("Aluno desativado com sucesso");
        await Promise.all([invalidateAlunos(), invalidateAlunoDetail(variables.alunoId)]);
      },
    },
  });

  const activateAluno = useActivateAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao ativar o aluno");
      },
      onSuccess: async (_, variables) => {
        toast.success("Aluno ativado com sucesso");
        await Promise.all([invalidateAlunos(), invalidateAlunoDetail(variables.alunoId)]);
      },
    },
  });

  const updateAluno = useUpdateAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao atualizar o aluno");
      },
      onSuccess: async (_, variables) => {
        toast.success("Aluno atualizado com sucesso");
        await Promise.all([invalidateAlunos(), invalidateAlunoDetail(variables.alunoId)]);
      },
    },
  });


  return {
    createAluno,
    activateAluno,
    deactivateAluno,
    updateAluno,
  };
}
