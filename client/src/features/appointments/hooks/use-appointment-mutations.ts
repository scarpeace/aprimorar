import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getAppointmentByIdQueryKey,
  getAppointmentsQueryKey,
  getAppointmentsByEmployeeIdQueryKey,
  getAppointmentsByStudentIdQueryKey,
  getEmployeeSummaryQueryKey,
  getFinanceSummaryQueryKey,
  getStudentSummaryQueryKey,
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
          queryKey: getAppointmentByIdQueryKey(createdAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAppointmentByIdQueryKey(createdAppointment.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        queryClient.invalidateQueries({ queryKey: ["finance", "employee-summary", createdAppointment.employeeId] });
        queryClient.invalidateQueries({ queryKey: ["finance", "student-summary", createdAppointment.studentId] });
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
          queryKey: getAppointmentByIdQueryKey(updatedAppointment.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getAppointmentByIdQueryKey(updatedAppointment.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        queryClient.invalidateQueries({ queryKey: ["finance", "employee-summary", updatedAppointment.employeeId] });
        queryClient.invalidateQueries({ queryKey: ["finance", "student-summary", updatedAppointment.studentId] });
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
          queryKey: getAppointmentByIdQueryKey(updatedAppointment.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        queryClient.invalidateQueries({ queryKey: getStudentSummaryQueryKey(updatedAppointment.studentId) });
        queryClient.invalidateQueries({ queryKey: ["finance", "student-summary", updatedAppointment.studentId] });
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
          queryKey: getAppointmentByIdQueryKey(updatedAppointment.employeeId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEmployeeSummaryQueryKey(updatedAppointment.employeeId) });
        queryClient.invalidateQueries({ queryKey: ["finance", "student-summary", updatedAppointment.studentId] });
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
