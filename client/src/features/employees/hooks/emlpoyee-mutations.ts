import {
  findColaboradorByIdQueryKey,
  listColaboradoresQueryKey,
  getColaboradoresQueryKey,
  useArquivarColaborador,
  useCreateColaborador,
  useDeleteColaborador,
  useDesarquivarColaborador,
  useUpdateColaborador,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";

export function useEmployeeMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createEmployee = useCreateColaborador({
    mutation: {
      onSuccess: (createdEmployee) => {
        toast.success("Colaborador criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
        queryClient.invalidateQueries({
          queryKey: listColaboradoresQueryKey(),
        });
        navigate(`/employees/${createdEmployee.id}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const updateEmployee = useUpdateColaborador({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
        queryClient.invalidateQueries({
          queryKey: findColaboradorByIdQueryKey(variables.colaboradorId),
        });
        queryClient.invalidateQueries({
          queryKey: listColaboradoresQueryKey(),
        });
        navigate(`/employees/${variables.colaboradorId}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const archiveEmployee = useArquivarColaborador({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador arquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
        queryClient.invalidateQueries({
          queryKey: findColaboradorByIdQueryKey(variables.colaboradorId),
        });
        queryClient.invalidateQueries({
          queryKey: listColaboradoresQueryKey(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const unarchiveEmployee = useDesarquivarColaborador({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Colaborador desarquivado com sucesso");
        queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
        queryClient.invalidateQueries({
          queryKey: findColaboradorByIdQueryKey(variables.colaboradorId),
        });
        queryClient.invalidateQueries({
          queryKey: listColaboradoresQueryKey(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const deleteEmployee = useDeleteColaborador({
    mutation: {
      onSuccess: () => {
        toast.success("Colaborador excluído com sucesso");
        queryClient.invalidateQueries({ queryKey: getColaboradoresQueryKey() });
        queryClient.invalidateQueries({
          queryKey: listColaboradoresQueryKey(),
        });
        navigate("/employees");
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
