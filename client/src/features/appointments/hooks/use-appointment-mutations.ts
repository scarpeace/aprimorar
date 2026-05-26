import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import {
  getAtendimentoByIdQueryKey,
  getFinanceReportQueryKey,
  getAtendimentosQueryKey,
  getAtendimentosByEmployeeIdQueryKey,
  getAtendimentosByStudentIdQueryKey,
  useCreateAtendimento,
  useToggleEmployeeAtendimentoPayment,
  useToggleStudentAtendimentoCharge,
  useUpdateAtendimento,
} from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

export function useAppointmentMutations() {
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
          queryKey: getAtendimentosByEmployeeIdQueryKey(createdAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAtendimentosByStudentIdQueryKey(createdAppointment.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: getFinanceReportQueryKey(),
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
          queryKey: getAtendimentosByEmployeeIdQueryKey(updatedAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAtendimentosByStudentIdQueryKey(updatedAppointment.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: getFinanceReportQueryKey(),
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
        queryClient.invalidateQueries({ queryKey: getAtendimentosByStudentIdQueryKey(updatedAppointment.studentId) });
        queryClient.invalidateQueries({
          queryKey: getFinanceReportQueryKey(),
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
        queryClient.invalidateQueries({ queryKey: getAtendimentosByEmployeeIdQueryKey(updatedAppointment.employeeId) });
        queryClient.invalidateQueries({
          queryKey: getFinanceReportQueryKey(),
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
