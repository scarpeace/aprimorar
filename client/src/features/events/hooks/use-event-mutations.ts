import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { getEventByIdQueryKey, getEventsQueryKey, useCreateEvent, useUpdateEvent } from "@/kubb";


export function useCreateEventMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateEvent({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (createdEvent) => {
        toast.success("Evento criado com sucesso");
        queryClient.invalidateQueries({queryKey: getEventsQueryKey()});
        navigate(`/events/${createdEvent.eventId}`);
      },
    },
  });
}

export function useUpdateEventMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useUpdateEvent({
    mutation: {
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
      onSuccess: (updatedEvent, variables) => {
        toast.success("Evento atualizado com sucesso");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.eventId) })
        navigate(`/events/${updatedEvent.eventId}`);
      },
    },
  });
}

