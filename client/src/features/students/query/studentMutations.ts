import {
  useArchiveStudent,
  useCreateStudent,
  useUnarchiveStudent,
  useUpdateStudent,
} from "@/kubb";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { studentsQueryKeys } from "./studentsQueryKeys";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

export function useCreateStudentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateStudent({
    mutation: {
      onSuccess: (createdStudent) => {
        toast.success("Aluno criado com sucesso");
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.lists(),
        });
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.summary(),
        });
        navigate(`/students/${createdStudent.id}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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

        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.detail(variables.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.summary(),
        });

        navigate(`/students/${updatedStudent.id}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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

        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.detail(variables.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.summary(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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

        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.detail(variables.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.summary(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}
