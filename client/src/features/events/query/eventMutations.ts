import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

import { eventQueryKeys } from "./eventQueryKeys";
import { useCreateEvent, useDeleteEvent, useUpdateEvent } from "@/kubb";

export function useCreateEventMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useCreateEvent({
    mutation: {
      onSuccess: (createdEvent) => {
        toast.success("Evento criado com sucesso");
        queryClient.invalidateQueries({
          queryKey: eventQueryKeys.lists(),
        });

        navigate(`/events/${createdEvent.eventId}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useDeleteEventMutation() {
  const queryClient = useQueryClient();

  return useDeleteEvent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Responsável excluído com sucesso");

        //TODO: talvez aqui dê pra juntar essas duas queries para todas as listas. Até porque o summary é uma lista, não?
        queryClient.invalidateQueries({ queryKey: eventQueryKeys.lists()});
        queryClient.invalidateQueries({queryKey: eventQueryKeys.detail(variables.eventId)});
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}

export function useUpdateEventMutation() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useUpdateEvent({
    mutation: {
      onSuccess: (updatedEvent, variables) => {
        toast.success("Evento atualizado com sucesso");

        queryClient.invalidateQueries({ queryKey: eventQueryKeys.lists() });
        queryClient.invalidateQueries({
          queryKey: eventQueryKeys.detail(variables.eventId),
        });

        navigate(`/events/${updatedEvent.eventId}`);
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error));
      },
    },
  });
}
