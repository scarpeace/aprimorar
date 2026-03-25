import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useCreateEmployee, useUpdateEmployee, useDeleteEmployee, useArchiveEmployee, useUnarchiveEmployee } from "@/kubb";
import { employeeQueryKeys } from "./employeeQueryKeys";

export function useCreateEmployeeMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateEmployee({
    mutation: {
      onSuccess: (createdEmployee) => {
        toast.success("Colaborador criado com sucesso");
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.list()});
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.summary()});
        navigate(`/employees/${createdEmployee.id}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useUpdateEmployeeMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useUpdateEmployee({
    mutation: {
      onSuccess: (updatedEmployee, variables) => {
        toast.success("Colaborador atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: employeeQueryKeys.lists() });
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.detail(variables.employeeId)});
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.summary()});

        navigate(`/employees/${updatedEmployee.id}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useDeleteEmployeeMutation() {
  const queryClient = useQueryClient();

  return useDeleteEmployee({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador excluído com sucesso");
        //TODO: talvez aqui dê pra juntar essas duas queries para todas as listas. Até porque o summary é uma lista, não?
        queryClient.invalidateQueries({ queryKey: employeeQueryKeys.lists() });
        queryClient.invalidateQueries({ queryKey: employeeQueryKeys.summary() });
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.detail(variables.employeeId)})
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useArchiveEmployeeMutation() {
  const queryClient = useQueryClient();

  return useArchiveEmployee({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: employeeQueryKeys.lists() });
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.summary()});
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.detail(variables.employeeId)});
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useUnarchiveEmployeeMutation() {
  const queryClient = useQueryClient();

  return useUnarchiveEmployee({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: employeeQueryKeys.lists() });
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.detail(variables.employeeId)});
        queryClient.invalidateQueries({queryKey: employeeQueryKeys.summary()});
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}
