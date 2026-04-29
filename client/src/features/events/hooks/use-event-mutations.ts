import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getEmployeeMonthlySummaryQueryKey,
  getEventByIdQueryKey,
  getEventsByEmployeeIdQueryKey,
  getEventsByStudentIdQueryKey,
  getEventsQueryKey,
  getFinanceSummaryQueryKey,
  useCreateEvent,
  useToggleEmployeeEventPayment,
  useToggleStudentEventCharge,
  useUpdateEvent,
} from "@/kubb";

export function useEventMutations() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  const createEvent = useCreateEvent({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
        return error;
      },
      onSuccess: (createdEvent) => {
        toast.success("Evento criado com sucesso");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({
          queryKey: getEventsByEmployeeIdQueryKey(createdEvent.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEmployeeMonthlySummaryQueryKey(createdEvent.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEventsByStudentIdQueryKey(createdEvent.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        navigate(`/events/${createdEvent.eventId}`);
      },
    },
  });

  const updateEvent = useUpdateEvent({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (updatedEvent, variables) => {
        toast.success("Evento atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({
          queryKey: getEventsByEmployeeIdQueryKey(updatedEvent.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEmployeeMonthlySummaryQueryKey(updatedEvent.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEventsByStudentIdQueryKey(updatedEvent.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        navigate(`/events/${updatedEvent.eventId}`);
      },
    },
  });

  const toggleStudentCharge = useToggleStudentEventCharge({
    mutation: {
      onSuccess: (updatedEvent, variables) => {
        toast.success("Status da cobrança atualizado");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({
          queryKey: getEventsByStudentIdQueryKey(updatedEvent.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  const toggleEmployeePayment = useToggleEmployeeEventPayment({
    mutation: {
      onSuccess: (updatedEvent, variables) => {
        toast.success("Status do pagamento atualizado");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.id) });
        queryClient.invalidateQueries({
          queryKey: getEventsByEmployeeIdQueryKey(updatedEvent.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEmployeeMonthlySummaryQueryKey(updatedEvent.employeeId),
        });
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });

  return {
    createEvent,
    updateEvent,
    toggleStudentCharge,
    toggleEmployeePayment,
  };
}
