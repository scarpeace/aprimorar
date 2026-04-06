import {
  deleteStudentMutationKey,
  getStudentByIdQueryKey,
  getStudentsQueryKey,
  useArchiveStudent,
  useCreateStudent,
  useDeleteStudent,
  useUnarchiveStudent,
  useUpdateStudent,
  type StudentResponseDTO,
} from "@/kubb";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useStudentMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createStudent = useCreateStudent({
    mutation: {
      onError: () => {
        toast.error("Algo deu errado ao criar o aluno");
      },
      onSuccess: (createdStudent: StudentResponseDTO) => {
        toast.success("Aluno criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        navigate(`/students/${createdStudent.id}`);
      },
    },
  });

  const updateStudent = useUpdateStudent({
    mutation: {
      onSuccess: (updatedStudent) => {
        toast.success("Aluno atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getStudentByIdQueryKey(updatedStudent.id),
        });
        navigate(`/students/${updatedStudent.id}`);
      },
      onError: () => {
        toast.error("Algo deu errado ao atualizar o aluno");
      },
    },
  });

  const deleteStudent = useDeleteStudent({
    mutation: {
      onSuccess: () => {
        toast.success("Aluno excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: deleteStudentMutationKey() });
      },
      onError: () => {
        toast.error("Algo deu errado ao excluir o aluno");
      },
    },
  });

  const archiveStudent = useArchiveStudent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Aluno arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getStudentByIdQueryKey(variables.studentId),
        });
      },
      onError: () => {
        toast.error("Algo deu errado ao arquivar o aluno");
      },
    },
  });

  const unarchiveStudent = useUnarchiveStudent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getStudentByIdQueryKey(variables.studentId),
        });
      },
      onError: () => {
        toast.error("Algo deu errado ao desarquivar o aluno");
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
