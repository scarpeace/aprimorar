"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useActivateAluno } from "@/lib/api/generated/hooks/alunos/useActivateAluno";
import { useMatricularAluno } from "@/lib/api/generated/hooks/alunos/useMatricularAluno";

import { useDeactivateAluno } from "@/lib/api/generated/hooks/alunos/useDeactivateAluno";
import { useUpdateAluno } from "@/lib/api/generated/hooks/alunos/useUpdateAluno";
import { getAlunoByIdQueryKey } from "@/lib/api/generated/hooks/alunos/useGetAlunoById";
import { getAlunosQueryKey } from "@/lib/api/generated/hooks/alunos/useGetAlunos";
import { listAlunosOptionsQueryKey } from "@/lib/api/generated/hooks/alunos/useListAlunosOptions";
import { getFriendlyErrorMessage } from "@/lib/api/api-error";

export function useAlunoMutations() {
  const queryClient = useQueryClient();

  function invalidateAlunos() {
    return Promise.all([
      queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() }),
      queryClient.invalidateQueries({ queryKey: listAlunosOptionsQueryKey() }),
    ]);
  }

  function invalidateAlunoDetail(alunoId: string) {
    return queryClient.invalidateQueries({ queryKey: getAlunoByIdQueryKey(alunoId) });
  }

  const matricularAluno = useMatricularAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao matricular o aluno");
      },
      onSuccess: async () => {
        toast.success("Aluno matriculado com sucesso");
        await invalidateAlunos();
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
    matricularAluno,
    activateAluno,
    deactivateAluno,
    updateAluno,
  };
}
