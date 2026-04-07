import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { ChevronDownCircle } from "lucide-react";

import { eventRequestDTOContentEnum, useGetEmployeeOptions, useGetEventById, useGetStudentsOptions } from "@/kubb";
import * as EventFormLayout from "../forms/EventFormLayout";
import {
  type EventFormSchema,
  eventFormSchema,
} from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";
import { toDatetimeLocalInput, toInstant } from "@/lib/utils/dateFormater";
import { useParams } from "react-router-dom";
import { EventContentLabels } from "@/lib/shared/eventContentLables";

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
    price: event?.price ?? 0,
    payment: event?.payment ?? 0,
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
