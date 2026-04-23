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
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useStudentMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createStudent = useCreateStudent({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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
