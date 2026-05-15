import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getAppointmentByIdQueryKey,
  getAppointmentFinanceReportQueryKey,
  getAppointmentsQueryKey,
  getAppointmentsByEmployeeIdQueryKey,
  getAppointmentsByStudentIdQueryKey,
  useCreateAppointment,
  useToggleEmployeeAppointmentPayment,
  useToggleStudentAppointmentCharge,
  useUpdateAppointment,
} from "@/kubb";

export function useAppointmentMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createAppointment = useCreateAppointment({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
        return error;
      },
      onSuccess: (createdAppointment) => {
        toast.success("Evento criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAppointmentsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getAppointmentsByEmployeeIdQueryKey(createdAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAppointmentsByStudentIdQueryKey(createdAppointment.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: getAppointmentFinanceReportQueryKey(),
        });
        navigate(`/appointments/${createdAppointment.id}`);
      },
    },
  });

  const updateAppointment = useUpdateAppointment({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (updatedAppointment, variables) => {
        toast.success("Evento atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getAppointmentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getAppointmentByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({
          queryKey: getAppointmentsByEmployeeIdQueryKey(updatedAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAppointmentsByStudentIdQueryKey(updatedAppointment.studentId),
        });
        queryClient.invalidateQueries({
          queryKey: getAppointmentFinanceReportQueryKey(),
        });
        navigate(`/appointments/${updatedAppointment.id}`);
      },
    },
  });

  const toggleStudentCharge = useToggleStudentAppointmentCharge({
    mutation: {
      onSuccess: (updatedAppointment, variables) => {
        toast.success("Status da cobrança atualizado");
        queryClient.invalidateQueries({ queryKey: getAppointmentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getAppointmentByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({ queryKey: getAppointmentsByStudentIdQueryKey(updatedAppointment.studentId) });
        queryClient.invalidateQueries({
          queryKey: getAppointmentFinanceReportQueryKey(),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const toggleEmployeePayment = useToggleEmployeeAppointmentPayment({
    mutation: {
      onSuccess: (updatedAppointment, variables) => {
        toast.success("Status do pagamento atualizado");
        queryClient.invalidateQueries({ queryKey: getAppointmentsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getAppointmentByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({ queryKey: getAppointmentsByEmployeeIdQueryKey(updatedAppointment.employeeId) });
        queryClient.invalidateQueries({
          queryKey: getAppointmentFinanceReportQueryKey(),
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
