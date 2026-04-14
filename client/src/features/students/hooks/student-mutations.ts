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
      onError: (error) => {
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
      onError: () => {
        toast.error("Algo deu errado ao atualizar o aluno");
      },
      onSuccess: (_,variables) => {
        toast.success("Aluno atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getStudentByIdQueryKey(variables.studentId),
        });
        navigate(`/students/${variables.studentId}`);
      },
    },
  });

  const deleteStudent = useDeleteStudent({
    mutation: {
      onError: () => {
        toast.error("Algo deu errado ao excluir o aluno");
      },
      onSuccess: () => {
        toast.success("Aluno excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: deleteStudentMutationKey() });
        navigate("/students");
      },
    },
  });

  const archiveStudent = useArchiveStudent({
    mutation: {
      onError: () => {
        toast.error("Algo deu errado ao arquivar o aluno");
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getStudentByIdQueryKey(variables.studentId),
        });
      },
    },
  });

  const unarchiveStudent = useUnarchiveStudent({
    mutation: {
      onError: () => {
        toast.error("Algo deu errado ao desarquivar o aluno");
      },
      onSuccess: (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getStudentByIdQueryKey(variables.studentId),
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
