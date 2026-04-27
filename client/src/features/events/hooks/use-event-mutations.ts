import { useQueryClient } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { toast } from "sonner";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import {
  getEventByIdQueryKey,
  getEventsQueryKey,
  useCreateEvent,
  useSettleEmployeePaymentEvent,
  useSettleStudentChargeEvent,
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
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.id) });
        if (onSuccessCallback) {
          onSuccessCallback();
        } else {
          navigate(`/events/${updatedEvent.eventId}`);
        }
      },
    },
  });


  const handleStatusError = (error: any) => {
    toast.error(getFriendlyErrorMessage(error));
  };

  const settleStudentCharge = useSettleStudentChargeEvent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Status da cobrança atualizado");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.id) });
      },
      onError: handleStatusError,
    },
  });

  const settleEmployeePayment = useSettleEmployeePaymentEvent({
    mutation: {
      onSuccess: (_, variables) => {
        toast.success("Status do pagamento atualizado");
        queryClient.invalidateQueries({ queryKey: getEventsQueryKey() });
        queryClient.invalidateQueries({ queryKey: getEventByIdQueryKey(variables.id) });
      },
      onError: handleStatusError,
    },
  });

  return {
    createEvent,
    updateEvent,
    settleStudentCharge,
    settleEmployeePayment,
  };
}
