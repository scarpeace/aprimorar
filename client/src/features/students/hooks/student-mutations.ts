import {
    deleteStudentMutationKey,
    getStudentByIdQueryKey,
    getStudentsQueryKey,
    useArchiveStudent,
    useCreateStudent,
    useDeleteStudent,
    useUnarchiveStudent,
    useUpdateStudent,
    type StudentResponseDTO
} from "@/kubb";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useCreateStudentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateStudent({
    mutation: {
      onError: () => {
        toast.error("Algo deu errado ao criar o aluno");
      },
      onSuccess: (createdStudent: StudentResponseDTO) => {
        toast.success("Aluno criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() })
        navigate(`/students/${createdStudent.id}`);
      },
    },
  });
}

export function useUpdateStudentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useUpdateStudent({
    mutation: {
      onSuccess: (updatedStudent) => {
        toast.success("Aluno atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() })
        queryClient.invalidateQueries({ queryKey: getStudentByIdQueryKey(updatedStudent.id) })
        navigate(`/students/${updatedStudent.id}`);
      },
      onError: () => {
        toast.error("Algo deu errado ao atualizar o aluno");
      },
    },
  });
}

export function useDeleteStudentMutation() {
  const queryClient = useQueryClient();

  return useDeleteStudent({
    mutation: {
      onSuccess: () => {
        toast.success("Aluno excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() })
        queryClient.invalidateQueries({ queryKey: deleteStudentMutationKey() });
      },
      onError: () => {
        toast.error("Algo deu errado ao excluir o aluno");
      },
    },
  });
}

export function useArchiveStudentMutation() {
  const queryClient = useQueryClient();

  return useArchiveStudent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Aluno arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() })
        queryClient.invalidateQueries({ queryKey: getStudentByIdQueryKey(variables.studentId) })
      },
      onError: () => {
        toast.error("Algo deu errado ao arquivar o aluno");
      },
    },
  });
}

export function useUnarchiveStudentMutation() {
  const queryClient = useQueryClient();

  return useUnarchiveStudent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getStudentByIdQueryKey(variables.studentId) });
      },
      onError: () => {
        toast.error("Algo deu errado ao desarquivar o aluno");
      },
    },
  });
}
