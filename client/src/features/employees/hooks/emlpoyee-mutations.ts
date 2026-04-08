import {
  getEmployeeByIdQueryKey,
  getEmployeeOptionsQueryKey,
  getEmployeesQueryKey,
  useArchiveEmployee,
  useCreateEmployee,
  useDeleteEmployee,
  useUnarchiveEmployee,
  useUpdateEmployee,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useEmployeeMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createEmployee = useCreateEmployee({
    mutation: {
      onSuccess: (createdEmployee) => {
        toast.success("Colaborador criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getEmployeesQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getEmployeeOptionsQueryKey(),
        });
        navigate(`/employees/${createdEmployee.id}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const updateEmployee = useUpdateEmployee({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getEmployeesQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getEmployeeByIdQueryKey(variables.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEmployeeOptionsQueryKey(),
        });
        navigate(`/employees/${variables.employeeId}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const archiveEmployee = useArchiveEmployee({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getEmployeesQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getEmployeeByIdQueryKey(variables.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEmployeeOptionsQueryKey(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const unarchiveEmployee = useUnarchiveEmployee({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getEmployeesQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getEmployeeByIdQueryKey(variables.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEmployeeOptionsQueryKey(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const deleteEmployee = useDeleteEmployee({
    mutation: {
      onSuccess: () => {
        toast.success("Colaborador excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getEmployeesQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getEmployeeOptionsQueryKey(),
        });
        navigate("/employees");
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  return {
    createEmployee,
    updateEmployee,
    archiveEmployee,
    unarchiveEmployee,
    deleteEmployee,
  };
}
