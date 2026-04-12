import { Alert } from "@/components/ui/alert";
import { PageLayout } from "@/components/layout/PageLayout";
import { zodResolver } from "@hookform/resolvers/zod";
import { Calendar } from "lucide-react";
import { useForm } from "react-hook-form";

import { toInstant } from "@/lib/utils/dateFormater";
import * as EventForm from "../forms/EventForm";
import {
  type EventFormSchema,
  eventFormSchema,
} from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";

export function EventCreatePage() {
  const { createEvent } = useEventMutations();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<EventFormSchema>({
    resolver: zodResolver(eventFormSchema),
    mode: "onBlur",
  });

  const onSubmit = handleSubmit((data: EventFormSchema) => {
    createEvent.mutate({
      data: {
        ...data,
        startDate: toInstant(data.startDate),
        endDate: toInstant(data.endDate),
      },
    });
  });

  const headerProps = {
    title: "Novo Atendimento",
    description: "Preencha abaixo os dados do evento.",
    Icon: Calendar,
    backLink: "/events",
  };

  return (
    <PageLayout {...headerProps}>
      <EventForm.Root
        title="Dados do evento"
        description="Informe data, valores e participantes do atendimento."
        onSubmit={onSubmit}
      >
        {createEvent.isError && (
          <Alert error={createEvent.error} variant="error" />
        )}

        <EventForm.Fields errors={errors} register={register} />

        <EventForm.Actions
          isSubmitting={createEvent.isPending}
          cancelTo="/events"
        />
      </EventForm.Root>
    </PageLayout>
  );
}
