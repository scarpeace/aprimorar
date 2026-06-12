import {
  getAlunoByIdQueryKey,
  getAlunosByResponsavelQueryKey,
  getAlunosQueryKey,
  getAtendimentosByAlunoQueryKey,
  listAlunosQueryKey,
  useArchiveAluno,
  useCriarAluno,
  useDeleteAluno,
  useUnarchiveAluno,
  useUpdateAluno,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useAlunoMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const invalidateAlunos = () => {
    queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
    queryClient.invalidateQueries({ queryKey: listAlunosQueryKey() });
  };

  const invalidateAlunoDetail = (alunoId: string, responsavelId?: string) => {
    queryClient.invalidateQueries({ queryKey: getAlunoByIdQueryKey(alunoId) });
    queryClient.invalidateQueries({ queryKey: getAtendimentosByAlunoQueryKey(alunoId) });

    if (responsavelId) {
      queryClient.invalidateQueries({ queryKey: getAlunosByResponsavelQueryKey(responsavelId) });
    }
  };

  const createAluno = useCriarAluno({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao criar o aluno",
        );
      },
      onSuccess: (createdAluno) => {
        toast.success("Aluno criado com sucesso");
        invalidateAlunos();
        invalidateAlunoDetail(createdAluno.id, createdAluno.responsavelId);
        navigate(`/alunos/${createdAluno.id}`);
      },
    },
  });

  const updateAluno = useUpdateAluno({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao atualizar o aluno",
        );
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno atualizado com sucesso");
        invalidateAlunos();
        invalidateAlunoDetail(variables.alunoId, variables.data.responsavelId);
        navigate(`/alunos/${variables.alunoId}`);
      },
    },
  });

  const deleteAluno = useDeleteAluno({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Aluno excluído com sucesso");
        invalidateAlunos();
        invalidateAlunoDetail(variables.alunoId);
        navigate("/alunos");
      },
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao excluir o aluno",
        );
      },
    },
  });

  const archiveAluno = useArchiveAluno({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao arquivar o aluno",
        );
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno arquivado com sucesso");
        invalidateAlunos();
        invalidateAlunoDetail(variables.alunoId);
      },
    },
  });

  const unarchiveAluno = useUnarchiveAluno({
    mutation: {
      onError: (error) => {
        toast.error(
          getFriendlyErrorMessage(error) ||
            "Algo deu errado ao desarquivar o aluno",
        );
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
        invalidateAlunos();
        invalidateAlunoDetail(variables.alunoId);
      },
    },
  });

  return {
    createAluno,
    updateAluno,
    deleteAluno,
    archiveAluno,
    unarchiveAluno,
  };
}
