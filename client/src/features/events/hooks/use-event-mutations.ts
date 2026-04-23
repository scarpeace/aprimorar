import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getEventByIdQueryKey,
  getEventsQueryKey,
  useCreateEvent,
  useUpdateEvent,
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

  return {
    createEvent,
    updateEvent,
  };
}
