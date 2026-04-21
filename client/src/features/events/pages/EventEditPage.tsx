import { PageLayout } from "@/components/layout/PageLayout";
import { Button, ButtonLink } from "@/components/ui/button";
import { DateTimeInput } from "@/components/ui/date-time-input";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { eventRequestDTOContentEnum, eventRequestDTOStatusEnum, useGetEventById } from "@/kubb";
import {
    toDatetimeLocalInput,
    toInstant
} from "@/lib/utils/dateFormater";
import { zodResolver } from "@hookform/resolvers/zod";
import { Calendar, TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { useParams } from "react-router-dom";

import { EmployeeSelectDropdown } from "@/features/employees/components/EmployeeSelectDropdown";
import { StudentSelectDropdown } from "@/features/students/components/StudentSelectDropdown";
import { ContentSelectDropdown } from "../components/ContentSelectDropdown";
import { StatusSelectDropdown } from "../components/StatusSelectDropdown";
import { type EventFormSchema, eventFormSchema } from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";

export function EventEditPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const eventQuery = useGetEventById(eventId);

  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<EventFormSchema>({
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
      status: eventQuery.data?.status ?? eventRequestDTOStatusEnum.SCHEDULED,
      description: eventQuery.data?.description ?? "",
    },
  });

  const { updateEvent } = useEventMutations();

  const onSubmit = handleSubmit((data: EventFormSchema) => {
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

        <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
          <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
            <EmployeeSelectDropdown
              registration={register("employeeId")}
              error={errors.employeeId?.message}
              label="Colaborador"
            />

            <StudentSelectDropdown
              registration={register("studentId")}
              error={errors.studentId?.message}
              label="Aluno"
            />

            <ContentSelectDropdown
              label="Atendimento"
              registration={register("content")}
              error={errors.content?.message}
            />

            <StatusSelectDropdown
              label="Status"
              registration={register("status")}
              error={errors.status?.message}
            />

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Data Início</legend>
              <DateTimeInput control={control} name="startDate" placeholderText="Início" />
              {errors?.startDate && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.startDate.message}</p>
              )}
            </fieldset>

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Data Fim</legend>
              <DateTimeInput control={control} name="endDate" placeholderText="Fim" />
              {errors?.endDate && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.endDate.message}</p>
              )}
            </fieldset>

            <div className="flex gap-3">
              <fieldset className="fieldset">
                <legend className="fieldset-legend">Preço (receita)</legend>
                <input type="number" className="input" placeholder="Preço (receita)" {...register("price", { valueAsNumber: true })} />
                {errors?.price && (
                  <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.price.message}</p>
                )}
              </fieldset>

              <fieldset className="fieldset">
                <legend className="fieldset-legend">Pagamento (custo)</legend>
                <input type="number" className="input" placeholder="Pagamento (custo)" {...register("payment", { valueAsNumber: true })} />
                {errors?.payment && (
                  <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.payment.message}</p>
                )}
              </fieldset>
            </div>

            <fieldset className="fieldset md:col-span-3">
              <legend className="fieldset-legend">Descrição (opcional)</legend>
              <textarea className="textarea textarea-bordered w-full" placeholder="Observações do atendimento" {...register("description")} />
              {errors?.description && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.description.message}</p>
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
