import { zodResolver } from "@hookform/resolvers/zod";
import { ChevronDownCircle, GraduationCap } from "lucide-react";
import { useForm } from "react-hook-form";

import { toInstant } from "@/lib/utils/dateFormater";
import * as EventForm from "../forms/EventForm";
import {
  type EventFormSchema,
  eventFormSchema,
} from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";
import { PageHeader } from "@/components/ui/page-header";
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
  });

  return (
    <>
      <PageHeader
        title="Novo Atendimento"
        description="Preencha abaixo os dados do evento."
        Icon={GraduationCap}
        backLink="/events"
      />

      <EventForm.Root
        title="Dados do evento"
        description="Informe data, valores e participantes do atendimento."
        onSubmit={onSubmit}
      >
        <EventForm.Fields
          errors={errors}
          register={register}
        />

        <EventForm.Actions
          isSubmitting={createEvent.isPending}
          cancelTo="/events"
        />
      </EventForm.Root>
    </>
  );
}
