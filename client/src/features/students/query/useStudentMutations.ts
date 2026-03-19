import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { dashboardQueryKeys } from "@/features/dashboard/query/dashboardQueryKeys";
import { eventsQueryKeys } from "@/features/events/query/eventsQueryKeys";
import { parentsQueryKeys } from "@/features/parents/query/parentsQueryKeys";
import { studentsApi } from "@/features/students/api/studentsApi";
import { studentsQueryKeys } from "@/features/students/query/studentsQueryKeys";
import type { StudentFormInput } from "@/features/students/schemas/student";

export function useCreateStudent() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: StudentFormInput) => studentsApi.create(data),
    onSuccess: (createdStudent) => {
      toast.success("Aluno criado com sucesso!");

      queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });

      navigate(`/students/${createdStudent.id}`);
    },
  });
}

export function useUpdateStudent(id: string) {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: StudentFormInput) => studentsApi.update(id, data),
    onSuccess: async (updatedStudent) => {
      navigate(`/students/${updatedStudent.id}`);
      toast.success("Aluno atualizado com sucesso!");

      await Promise.all([
        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() }),
        queryClient.invalidateQueries({
          queryKey: studentsQueryKeys.detail(id),
        }),
        queryClient.invalidateQueries({ queryKey: parentsQueryKeys.all }),
        queryClient.invalidateQueries({ queryKey: eventsQueryKeys.all }),
        queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all }),
      ]);
    },
  });
}

export function useDeleteStudent() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (id: string) => studentsApi.delete(id),
    onSuccess: async () => {
      toast.success("Aluno deletado com sucesso!");
      navigate("/students");

      queryClient.invalidateQueries({ queryKey: studentsQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: studentsQueryKeys.byParentPrefix(),
      });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });
    },
  });
}

export function useArchiveStudent() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => studentsApi.archive(id),
    onSuccess: async () => {
      toast.success("Aluno arquivado com sucesso!");

      await Promise.all([
        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.all }),
        queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all }),
      ]);
    },
  });
}

export function useUnarchiveStudent() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => studentsApi.unarchive(id),
    onSuccess: async () => {
      toast.success("Aluno desarquivado com sucesso!");

      await Promise.all([
        queryClient.invalidateQueries({ queryKey: studentsQueryKeys.all }),
        queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all }),
      ]);
    },
  });
}
