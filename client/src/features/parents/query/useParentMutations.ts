import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

import { dashboardQueryKeys } from "@/features/dashboard/query/dashboardQueryKeys";
import { parentsApi } from "@/features/parents/api/parentsApi";
import { parentsQueryKeys } from "@/features/parents/query/parentsQueryKeys";
import { studentsQueryKeys } from "@/features/students/query/studentsQueryKeys";
import type { ParentFormInput } from "@/lib/schemas";

export function useCreateParent() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: ParentFormInput) => parentsApi.create(data),
    onSuccess: (createdParent) => {
      toast.success("Responsável criado com sucesso!");

      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });

      navigate(`/parents/${createdParent.id}`);
    },
  });
}

export function useUpdateParent(id: string) {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: ParentFormInput) => parentsApi.update(id, data),
    onSuccess: () => {
      toast.success("Responsável atualizado com sucesso!");

      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.detail(id) });

      navigate(`/parents/${id}`);
    },
  });
}

export function useDeleteParent() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (id: string) => parentsApi.delete(id),
    onSuccess: async () => {
      toast.success("Responsável deletado com sucesso!");

      navigate("/parents");
      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: studentsQueryKeys.byParentPrefix(),
      });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });
    },
  });
}

export function useArchiveParent() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => parentsApi.archive(id),
    onSuccess: (_, id) => {
      toast.success("Responsável arquivado com sucesso!");

      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.detail(id) });
    },
  });
}

export function useUnarchiveParent() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => parentsApi.unarchive(id),
    onSuccess: (_, id) => {
      toast.success("Responsável desarquivado com sucesso!");

      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.all });
      queryClient.invalidateQueries({ queryKey: parentsQueryKeys.detail(id) });
    },
  });
}
