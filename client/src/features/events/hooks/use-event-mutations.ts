import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getEventByIdQueryKey,
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
          queryKey: getEventByIdQueryKey(createdEvent.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEventByIdQueryKey(createdEvent.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        queryClient.invalidateQueries({ queryKey: ["finance", "employee-summary", createdEvent.employeeId] });
        queryClient.invalidateQueries({ queryKey: ["finance", "student-summary", createdEvent.studentId] });
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
          queryKey: getEventByIdQueryKey(updatedEvent.employeeId),
        });
        queryClient.invalidateQueries({
          queryKey: getEventByIdQueryKey(updatedEvent.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        queryClient.invalidateQueries({ queryKey: ["finance", "employee-summary", updatedEvent.employeeId] });
        queryClient.invalidateQueries({ queryKey: ["finance", "student-summary", updatedEvent.studentId] });
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
          queryKey: getEventByIdQueryKey(updatedEvent.studentId),
        });
        queryClient.invalidateQueries({ queryKey: getFinanceSummaryQueryKey() });
        queryClient.invalidateQueries({ queryKey: ["finance", "student-summary", updatedEvent.studentId] });
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
          queryKey: getEventByIdQueryKey(updatedEvent.employeeId),
        });
        queryClient.invalidateQueries({ queryKey: ["finance", "employee-summary", updatedEvent.employeeId] });
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
