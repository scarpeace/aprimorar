import { zodResolver } from "@hookform/resolvers/zod";
import { ChevronDownCircle } from "lucide-react";
import { useForm } from "react-hook-form";

import { eventRequestDTOContentEnum, useGetEventById } from "@/kubb";
import { toDatetimeLocalInput, toInstant } from "@/lib/utils/dateFormater";
import { useParams } from "react-router-dom";
import * as EventFormLayout from "../forms/EventForm";
import {
    type EventFormSchema,
    eventFormSchema,
} from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";
import { DevTool } from "@hookform/devtools";

export function EventEditPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const {data: event} = useGetEventById(eventId)
  const form = useForm<EventFormSchema>({
  resolver: zodResolver(eventFormSchema),
  mode: "onBlur",
  values: {
    studentId: event?.studentId ?? "",
    employeeId: event?.employeeId ?? "",
    startDate: toDatetimeLocalInput(event?.startDate),
    endDate: toDatetimeLocalInput(event?.endDate),
    payment: event?.payment ?? 0,
    price: event?.price ?? 0,
    content: event?.content ?? eventRequestDTOContentEnum.AULA,
    title: event?.title ?? "",
    description: event?.description ?? "",
  },
});

  const { updateEvent } = useEventMutations();

  const onSubmit = form.handleSubmit((data: EventFormSchema) => {
  updateEvent.mutate({
    eventId,
    data: {
      ...data,
      startDate: toInstant(data.startDate),
      endDate: toInstant(data.endDate),
    },
  });
});

  return (
    <>
      <EventFormLayout.Header
        title="Editar evento"
        description="Edite os dados do evento."
        icon={ChevronDownCircle}
      />
      <DevTool control={form.control}/>

      <EventFormLayout.Root
        title="Dados do evento"
        description="Informe data, valores e participantes do atendimento."
        onSubmit={onSubmit}
      >
        <EventFormLayout.Fields
          errors={form.formState.errors}
          register={form.register}
        />

        <EventFormLayout.Actions
          isSubmitting={updateEvent.isPending}
          cancelTo="/events"
          submitLabel="Salvar"
          submittingLabel="Salvando..."
        />
      </EventFormLayout.Root>
    </>
  );
}
