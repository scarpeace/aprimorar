import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

import { dashboardQueryKeys } from "@/features/dashboard/query/dashboardQueryKeys";
import { employeesApi } from "@/features/employees/api/employeesApi";
import { employeesQueryKeys } from "@/features/employees/query/employeesQueryKeys";
import { eventsQueryKeys } from "@/features/events/query/eventsQueryKeys";
import type { EmployeeFormInput } from "@/features/employees/schemas/employee";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

export function useCreateEmployee() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: EmployeeFormInput) => employeesApi.create(data),
    onSuccess: (createdEmployee) => {
      toast.success("Colaborador criado com sucesso!");
      queryClient.invalidateQueries({ queryKey: employeesQueryKeys.lists() });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });
      navigate(`/employees/${createdEmployee.id}`);
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error));
    },
  });
}

export function useUpdateEmployee(id: string) {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (data: EmployeeFormInput) => employeesApi.update(id, data),
    onSuccess: () => {
      toast.success("Colaborador atualizado com sucesso!");
      queryClient.invalidateQueries({ queryKey: employeesQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: employeesQueryKeys.detail(id),
      });
      navigate(`/employees/${id}`);
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error));
    },
  });
}

export function useDeleteEmployee() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (id: string) => employeesApi.delete(id),
    onSuccess: async () => {
      toast.success("Colaborador excluído com sucesso!");
      navigate("/employees");
      queryClient.invalidateQueries({ queryKey: employeesQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: eventsQueryKeys.byEmployeePrefix(),
      });
      queryClient.invalidateQueries({ queryKey: dashboardQueryKeys.all });
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error));
    },
  });
}

export function useArchiveEmployee() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => employeesApi.archive(id),
    onSuccess: (_, id) => {
      toast.success("Colaborador arquivado com sucesso!");
      queryClient.invalidateQueries({ queryKey: employeesQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: employeesQueryKeys.detail(id),
      });
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error));
    },
  });
}

export function useUnarchiveEmployee() {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (id: string) => employeesApi.unarchive(id),
    onSuccess: (_, id) => {
      toast.success("Colaborador desarquivado com sucesso!");
      queryClient.invalidateQueries({ queryKey: employeesQueryKeys.lists() });
      queryClient.invalidateQueries({
        queryKey: employeesQueryKeys.detail(id),
      });
    },
    onError: (error) => {
      toast.error(getFriendlyErrorMessage(error));
    },
  });
}
