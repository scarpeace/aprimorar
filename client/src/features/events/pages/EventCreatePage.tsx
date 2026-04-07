import { zodResolver } from "@hookform/resolvers/zod";
import { ChevronDownCircle } from "lucide-react";
import { useForm } from "react-hook-form";

import { toInstant } from "@/lib/utils/dateFormater";
import * as EventFormLayout from "../forms/EventFormLayout";
import {
  type EventFormSchema,
  eventFormSchema,
} from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";
export function EventCreatePage() {
  const {
    register,
    handleSubmit,
    formState: { errors },
    control,
  } = useForm<EventFormSchema>({
    resolver: zodResolver(eventFormSchema),
    mode: "onBlur",
  });

  const { createEvent } = useEventMutations();

  const onSubmit = handleSubmit((data: EventFormSchema) => {
    createEvent.mutate({
      data: {
        ...data,
        startDate: toInstant(data.startDate),
        endDate: toInstant(data.endDate),
      },
    });
    console.log(toInstant(data.startDate), toInstant(data.endDate))
  });

  return (
    <>
      <EventFormLayout.Header
        title="Novo evento"
        description="Crie um novo atendimento/aula."
        icon={ChevronDownCircle}
      />

      <EventFormLayout.Root
        title="Dados do evento"
        description="Informe data, valores e participantes do atendimento."
        onSubmit={onSubmit}
      >
        <EventFormLayout.Fields
          errors={errors}
          register={register}
          control={control}
        />

        <EventFormLayout.Actions
          isSubmitting={createEvent.isPending}
          cancelTo="/events"
          submitLabel="Criar evento"
          submittingLabel="Salvando..."
        />
      </EventFormLayout.Root>
    </>
  );
}
