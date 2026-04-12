import { Alert } from "@/components/ui/alert";
import { PageLayout } from "@/components/layout/PageLayout";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { eventRequestDTOContentEnum, useGetEventById } from "@/kubb";
import { toDatetimeLocalInput, toInstant } from "@/lib/utils/dateFormater";
import { zodResolver } from "@hookform/resolvers/zod";
import { Calendar } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";

import * as EventForm from "../forms/EventForm";
import {
  type EventFormSchema,
  eventFormSchema,
} from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";

export function EventEditPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const {
    data: event,
    isPending: isEventPending,
    error: eventError,
  } = useGetEventById(eventId);

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

  const headerProps = {
    title: "Editar evento",
    description: "Edite os dados do evento.",
    Icon: Calendar,
    backLink: `/events/${eventId}`,
  };

  if (eventError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do evento"
          error={eventError}
        />
      </PageLayout>
    );
  }

  if (isEventPending || !event) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do evento" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <EventForm.Root
        title="Dados do evento"
        description="Informe data, valores e participantes do atendimento."
        onSubmit={onSubmit}
      >
        {updateEvent.isError && (
          <Alert error={updateEvent.error} variant="error" />
        )}

        <EventForm.Fields
          errors={form.formState.errors}
          register={form.register}
        />

        <EventForm.Actions
          isSubmitting={updateEvent.isPending}
          cancelTo={`/events/${eventId}`}
          submitLabel="Salvar"
          submittingLabel="Salvando..."
        />
      </EventForm.Root>
    </PageLayout>
  );
}
