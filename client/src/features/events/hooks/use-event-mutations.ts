import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getEventByIdQueryKey,
  getEventsQueryKey,
  useCreateEvent,
  useUpdateEvent,
  useCompleteEvent,
  useCancelEvent,
  useRescheduleEvent,
  useSettleIncomeEvent,
  useSettleExpenseEvent,
  type EventResponseDTO,
} from "@/kubb";

interface UseEventMutationsProps {
  onSuccessCallback?: () => void;
}

export function useEventMutations({ onSuccessCallback }: UseEventMutationsProps = {}) {
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
        if (onSuccessCallback) {
          onSuccessCallback();
        } else {
          navigate(`/events/${createdEvent.eventId}`);
        }
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
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.eventId) });
        if (onSuccessCallback) {
          onSuccessCallback();
        } else {
          navigate(`/events/${updatedEvent.eventId}`);
        }
      },
    },
  });

  const handleStatusSuccess = (message: string, eventId: string) => {
    toast.success(message);
    queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
    queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(eventId) });
    if (onSuccessCallback) {
      onSuccessCallback();
    }
  };

  const handleStatusError = (error: any) => {
    toast.error(getFriendlyErrorMessage(error));
  };

  const completeEvent = useCompleteEvent({
    mutation: {
      onSuccess: (_, variables) => handleStatusSuccess("Evento concluído com sucesso", variables.id),
      onError: handleStatusError,
    }
  });

  const cancelEvent = useCancelEvent({
    mutation: {
      onSuccess: (_, variables) => handleStatusSuccess("Evento cancelado com sucesso", variables.id),
      onError: handleStatusError,
    }
  });

  const rescheduleEvent = useRescheduleEvent({
    mutation: {
      onSuccess: (_, variables) => handleStatusSuccess("Evento re-agendado com sucesso", variables.id),
      onError: handleStatusError,
    }
  });

  const settleIncomeEvent = useSettleIncomeEvent({
    mutation: {
      onSuccess: (_, variables) => handleStatusSuccess("Baixa de recebimento atualizada", variables.id),
      onError: handleStatusError,
    }
  });

  const settleExpenseEvent = useSettleExpenseEvent({
    mutation: {
      onSuccess: (_, variables) => handleStatusSuccess("Baixa de pagamento atualizada", variables.id),
      onError: handleStatusError,
    }
  });

  const changeEventStatus = (
    event: EventResponseDTO,
    newStatus: "SCHEDULED" | "COMPLETED" | "CANCELED"
  ) => {
    if (newStatus === "COMPLETED") {
      completeEvent.mutate({ id: event.eventId });
    } else if (newStatus === "CANCELED") {
      cancelEvent.mutate({ id: event.eventId });
    } else if (newStatus === "SCHEDULED") {
      rescheduleEvent.mutate({ id: event.eventId });
    }
  };

  const isStatusPending =
    completeEvent.isPending ||
    cancelEvent.isPending ||
    rescheduleEvent.isPending;

  return {
    createEvent,
    updateEvent,
    changeEventStatus,
    isStatusPending,
    settleIncomeEvent,
    settleExpenseEvent,
  };
}
