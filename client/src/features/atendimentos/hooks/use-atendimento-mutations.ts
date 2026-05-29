import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import {
  getAtendimentoByIdQueryKey,
  getAtendimentosQueryKey,
  useCreateAtendimento,
  useToggleEmployeeAtendimentoPayment,
  useToggleStudentAtendimentoCharge,
  useUpdateAtendimento,
  getIndicadoresAtendimentosQueryKey,
  getAtendimentosByColaboradorQueryKey,
  getAtendimentosByAlunoQueryKey,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

export function useAtendimentoMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createAppointment = useCreateAtendimento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
        return error;
      },
      onSuccess: (createdAppointment) => {
        toast.success("Evento criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getAtendimentosByColaboradorQueryKey(createdAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAtendimentosByAlunoQueryKey(createdAppointment.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: getIndicadoresAtendimentosQueryKey(),
        });
        navigate(`/appointments/${createdAppointment.id}`);
      },
    },
  });

  const updateAppointment = useUpdateAtendimento({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (updatedAppointment, variables) => {
        toast.success("Evento atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey() });
        queryClient.invalidateQueries({ queryKey: getAtendimentoByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({
          queryKey: getAtendimentosByColaboradorQueryKey(updatedAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAtendimentosByAlunoQueryKey(updatedAppointment.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: getIndicadoresAtendimentosQueryKey(),
        });
        navigate(`/appointments/${updatedAppointment.id}`);
      },
    },
  });

  const toggleStudentCharge = useToggleStudentAtendimentoCharge({
    mutation: {
      onSuccess: (updatedAppointment, variables) => {
        toast.success("Status da cobrança atualizado");
        queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey() });
        queryClient.invalidateQueries({ queryKey: getAtendimentoByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({ queryKey: getAtendimentosByAlunoQueryKey(updatedAppointment.studentId) });
        queryClient.invalidateQueries({
          queryKey: getIndicadoresAtendimentosQueryKey(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const toggleEmployeePayment = useToggleEmployeeAtendimentoPayment({
    mutation: {
      onSuccess: (updatedAppointment, variables) => {
        toast.success("Status do pagamento atualizado");
        queryClient.invalidateQueries({ queryKey: getAtendimentosQueryKey() });
        queryClient.invalidateQueries({ queryKey: getAtendimentoByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({ queryKey: getAtendimentosByColaboradorQueryKey(updatedAppointment.employeeId) });
        queryClient.invalidateQueries({
          queryKey: getIndicadoresAtendimentosQueryKey(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  return {
    createAppointment,
    updateAppointment,
    toggleStudentCharge,
    toggleEmployeePayment,
  };
}
