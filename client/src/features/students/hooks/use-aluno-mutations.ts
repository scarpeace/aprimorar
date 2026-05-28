import {
  deletarAlunoMutationKey,
  buscarAlunoPorIdQueryKey,
  getAlunosQueryKey,
  useArquivarAluno,
  useCriarAluno,
  useDeletarAluno,
  useDesarquivarAluno,
  useAtualizarAluno,
  type AlunoResponseDTO,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useAlunoMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createStudent = useCriarAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (createdStudent: AlunoResponseDTO) => {
        toast.success("Aluno criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        navigate(`/students/${createdStudent.id}`);
      },
    },
  });

  const updateStudent = useAtualizarAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (_,variables) => {
        toast.success("Aluno atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        queryClient.invalidateQueries({
          queryKey: buscarAlunoPorIdQueryKey(variables.studentId),
        });
        navigate(`/students/${variables.studentId}`);
      },
    },
  });

  const deleteStudent = useDeletarAluno({
    mutation: {
      onSuccess: () => {
        toast.success("Aluno excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        queryClient.invalidateQueries({ queryKey: deletarAlunoMutationKey() });
        navigate("/alunos");
      },
    },
  });

  const archiveStudent = useArquivarAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        queryClient.invalidateQueries({
          queryKey: buscarAlunoPorIdQueryKey(variables.studentId),
        });
      },
    },
  });

  const unarchiveStudent = useDesarquivarAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        queryClient.invalidateQueries({
          queryKey: buscarAlunoPorIdQueryKey(variables.studentId),
        });
      },
    },
  });

  return {
    createStudent,
    updateStudent,
    deleteStudent,
    archiveStudent,
    unarchiveStudent,
  };
}
