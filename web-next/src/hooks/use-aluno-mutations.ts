"use client";

import { useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";
import { useArchiveAluno } from "@/lib/api/generated/hooks/aluno/useArchiveAluno";
import { useUnarchiveAluno } from "@/lib/api/generated/hooks/aluno/useUnarchiveAluno";
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

  return {
    archiveAluno,
    unarchiveAluno,
  };
}
