import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getEventByIdQueryKey,
  getEventsQueryKey,
  useCreateEvent,
  useUpdateEvent,
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

  const changeEventStatus = (
    event: EventResponseDTO,
    newStatus: "SCHEDULED" | "COMPLETED" | "CANCELED"
  ) => {
    updateEvent.mutate({
      eventId: event.eventId,
      data: {
        studentId: event.studentId,
        employeeId: event.employeeId,
        startDate: event.startDate,
        endDate: event.endDate,
        payment: event.payment,
        price: event.price,
        content: event.content,
        description: event.description || "",
        status: newStatus,
      },
    });
  };

  return {
    createEvent,
    updateEvent,
    changeEventStatus,
  };
}
