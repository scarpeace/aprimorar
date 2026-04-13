import { Alert } from "@/components/ui/alert";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button, ButtonLink } from "@/components/ui/button";
import { DateTimeInput } from "@/components/ui/date-time-input";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { eventRequestDTOContentEnum, useGetEventById } from "@/kubb";
import {
  fromDateToDatetimeLocalInput,
  toDatetimeLocalInput,
  toInstant,
} from "@/lib/utils/dateFormater";
import { zodResolver } from "@hookform/resolvers/zod";
import { Calendar, TriangleAlert } from "lucide-react";
import { Controller, useForm } from "react-hook-form";
import { useParams } from "react-router-dom";

import { ContentSelectDropdown } from "../components/ContentSelectDropdown";
import { EmployeeSelectDropdown } from "@/features/employees/components/EmployeeSelectDropdown";
import { StudentSelectDropdown } from "@/features/students/components/StudentSelectDropdown";
import { type EventFormSchema, eventFormSchema } from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";

export function EventEditPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const eventQuery = useGetEventById(eventId);

  const form = useForm<EventFormSchema>({
    resolver: zodResolver(eventFormSchema),
    mode: "onBlur",
    values: {
      studentId: eventQuery.data?.studentId ?? "",
      employeeId: eventQuery.data?.employeeId ?? "",
      startDate: toDatetimeLocalInput(eventQuery.data?.startDate),
      endDate: toDatetimeLocalInput(eventQuery.data?.endDate),
      payment: eventQuery.data?.payment ?? 0,
      price: eventQuery.data?.price ?? 0,
      content: eventQuery.data?.content ?? eventRequestDTOContentEnum.AULA,
      title: eventQuery.data?.title ?? "",
      description: eventQuery.data?.description ?? "",
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
    title: "Editar atendimento",
    description: "Edite os dados do atendimento.",
    Icon: Calendar,
    backLink: `/events/${eventId}`,
  };

  if (eventQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard title="Erro ao carregar detalhes do atendimento" error={eventQuery.error} />
      </PageLayout>
    );
  }

  if (eventQuery.isPending || !eventQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do atendimento" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <SectionCard
        title="Dados do atendimento"
        description="Informe data, valores e participantes do atendimento."
      >
        {updateEvent.isError && (
          <Alert error={updateEvent.error} variant="error" />
        )}

        <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
            <EmployeeSelectDropdown
              registration={form.register("employeeId")}
              error={form.formState.errors.employeeId?.message}
              label="Colaborador"
            />

            <StudentSelectDropdown
              registration={form.register("studentId")}
              error={form.formState.errors.studentId?.message}
              label="Aluno"
            />

            <ContentSelectDropdown
              label="Atendimento"
              registration={form.register("content")}
              error={form.formState.errors.content?.message}
            />

            <Controller
              control={form.control}
              name="startDate"
              render={({ field }) => (
                <DateTimeInput
                  label="Início"
                  selected={field.value ? new Date(field.value) : null}
                  onChange={(date) => field.onChange(fromDateToDatetimeLocalInput(date))}
                  error={form.formState.errors.startDate?.message}
                  placeholderText="Selecione a data de início"
                />
              )}
            />

            <Controller
              control={form.control}
              name="endDate"
              render={({ field }) => (
                <DateTimeInput
                  label="Fim"
                  selected={field.value ? new Date(field.value) : null}
                  onChange={(date) => field.onChange(fromDateToDatetimeLocalInput(date))}
                  error={form.formState.errors.endDate?.message}
                  placeholderText="Selecione a data de fim"
                />
              )}
            />

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Título</legend>
              <input type="text" className="input" placeholder="Ex: Aula de matemática" {...form.register("title")} />
              {form.formState.errors?.title && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {form.formState.errors.title.message}</p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Preço (receita)</legend>
              <input type="number" className="input" placeholder="Preço (receita)" {...form.register("price", { valueAsNumber: true })} />
              {form.formState.errors?.price && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {form.formState.errors.price.message}</p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Pagamento (custo)</legend>
              <input type="number" className="input" placeholder="Pagamento (custo)" {...form.register("payment", { valueAsNumber: true })} />
              {form.formState.errors?.payment && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {form.formState.errors.payment.message}</p>
              )}
            </fieldset>

            <fieldset className="fieldset md:col-span-3">
              <legend className="fieldset-legend">Descrição (opcional)</legend>
              <textarea className="textarea textarea-bordered w-full" placeholder="Observações do atendimento" {...form.register("description")} />
              {form.formState.errors?.description && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {form.formState.errors.description.message}</p>
              )}
            </fieldset>
          </div>

          <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <ButtonLink to={`/events/${eventId}`} variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={updateEvent.isPending} variant="primary">
              {updateEvent.isPending ? "Salvando..." : "Salvar"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </PageLayout>
  );
}
