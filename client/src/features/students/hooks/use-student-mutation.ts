import {
  getEventsQueryKey,
  getParentsQueryKey,
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
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getParentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.setQueryData(
          getStudentByIdQueryKey(createdStudent.id),
          createdStudent,
        );
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
      onSuccess: (updatedStudent, variables) => {
        toast.success("Aluno atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
        navigate(`/students/${updatedStudent.id}`);
      },
      onError: (error) => {
        toast.error("Algo deu errado ao atualizar o aluno");
      },
    },
  });
}

export function useDeleteStudentMutation() {
  const queryClient = useQueryClient();

  return useDeleteStudent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Aluno excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
      },
      onError: (error) => {
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
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
      },
      onError: (error) => {
        toast.error("Algo deu errado ao arquivar o aluno");
      },
    },
  });
}

//TODO: aparentemente a palavra "Unarchive" tá errada
export function useUnarchiveStudentMutation() {
  const queryClient = useQueryClient();

  return useUnarchiveStudent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Aluno desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
      },
      onError: (error) => {
        toast.error("Algo deu errado ao desarquivar o aluno");
      },
    },
  });
}
