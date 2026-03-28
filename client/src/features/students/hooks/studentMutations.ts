import {
  getStudentsQueryKey,
  updateStudentMutationKey,
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
import { studentsQueryKeys } from "./studentQueryKeys";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { studentInputSchema } from "./studentSchema";

export function useCreateStudentMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateStudent({
    mutation: {
      // onMutate: (newStudent) => {
      //   const { success, error, data } = studentInputSchema.safeParse(newStudent);
      //   if (!success) {
      //     console.log("ERRO DO ZOD:", error)
      //     throw error;
      //   }
      //   return data;
      // },
      onError: (error) => {
        toast.error("Algo deu errado ao criar o aluno");
      },
      onSuccess: (createdStudent: StudentResponseDTO) => {
        toast.success("Aluno criado com sucesso");
        queryClient.invalidateQueries({ queryKey: updateStudentMutationKey() });
        queryClient.invalidateQueries({ queryKey: getStudentsQueryKey() });
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
        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
        queryClient.invalidateQueries({queryKey: studentsQueryKeys.detail(variables.studentId)});
        queryClient.invalidateQueries({queryKey: studentsQueryKeys.summary()});

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
        //TODO: talvez aqui dê pra juntar essas duas queries para todas as listas. Até porque o summary é uma lista, não?
        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.summary() });
        queryClient.invalidateQueries({queryKey: studentsQueryKeys.detail(variables.studentId)})
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
        queryClient.invalidateQueries({queryKey: studentsQueryKeys.summary()});
        queryClient.invalidateQueries({queryKey: studentsQueryKeys.detail(variables.studentId)});
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
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
        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
        queryClient.invalidateQueries({queryKey: studentsQueryKeys.detail(variables.studentId)});
        queryClient.invalidateQueries({queryKey: studentsQueryKeys.summary()});
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}
