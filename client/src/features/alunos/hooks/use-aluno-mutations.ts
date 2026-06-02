import {
  getAlunoByIdQueryKey,
  getAlunosQueryKey,
  useArchiveAluno,
  useCriarAluno,
  useDeleteAluno,
  useUnarchiveAluno,
  useUpdateAluno,
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

  const updateStudent = useUpdateAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (_,variables) => {
        toast.success("Aluno atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getAlunoByIdQueryKey(variables.studentId),
        });
        navigate(`/students/${variables.studentId}`);
      },
    },
  });

  const deleteStudent = useDeleteAluno({
    mutation: {
      onSuccess: () => {
        toast.success("Aluno excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        navigate("/alunos");
      },
    },
  });

  const archiveStudent = useArchiveAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getAlunoByIdQueryKey(variables.studentId),
        });
      },
    },
  });

  const unarchiveStudent = useUnarchiveAluno({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAlunosQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getAlunoByIdQueryKey(variables.studentId),
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
