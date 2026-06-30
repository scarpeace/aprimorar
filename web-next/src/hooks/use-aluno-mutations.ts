"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useArchiveAluno } from "@/lib/api/generated/hooks/aluno/useArchiveAluno";
import { useCriarAluno } from "@/lib/api/generated/hooks/aluno/useCriarAluno";
import { useUnarchiveAluno } from "@/lib/api/generated/hooks/aluno/useUnarchiveAluno";
import { useUpdateAluno } from "@/lib/api/generated/hooks/aluno/useUpdateAluno";
import { getAlunoByIdQueryKey } from "@/lib/api/generated/hooks/aluno/useGetAlunoById";
import { getAlunosQueryKey } from "@/lib/api/generated/hooks/aluno/useGetAlunos";
import { getAlunosKpisQueryKey } from "@/lib/api/generated/hooks/aluno/useGetAlunosKpis";
import { getFriendlyErrorMessage } from "@/lib/api/client";

export function useAlunoMutations() {
  const queryClient = useQueryClient();

  function invalidateAlunos() {
    queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
    queryClient.invalidateQueries({ queryKey: getAlunosKpisQueryKey() });
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

  const archiveAluno = useArchiveAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao arquivar o aluno");
      },
      onSuccess: async (_, variables) => {
        toast.success("Aluno arquivado com sucesso");
        await Promise.all([invalidateAlunos(), invalidateAlunoDetail(variables.alunoId)]);
      },
    },
  });

  const unarchiveAluno = useUnarchiveAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Algo deu errado ao desarquivar o aluno");
      },
      onSuccess: async (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
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
    archiveAluno,
    unarchiveAluno,
    updateAluno,
  };
}
