import { Alert } from "@/components/ui/alert";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button, ButtonLink } from "@/components/ui/button";
import { SectionCard } from "@/components/ui/section-card";
import { zodResolver } from "@hookform/resolvers/zod";
import { Calendar, TriangleAlert } from "lucide-react";
import { Controller, useForm } from "react-hook-form";

import { DevTool } from "@hookform/devtools";
import {
  fromDateToDatetimeLocalInput,
  toInstant,
} from "@/lib/utils/dateFormater";
import { ContentSelectDropdown } from "../components/ContentSelectDropdown";
import { EmployeeSelectDropdown } from "@/features/employees/components/EmployeeSelectDropdown";
import { StudentSelectDropdown } from "@/features/students/components/StudentSelectDropdown";
import {
  type EventFormSchema,
  eventFormSchema,
} from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";
import { DateTimeInput } from "@/components/ui/date-time-input";
import DatePicker from "react-datepicker";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

export function EventCreatePage() {
  const { createEvent } = useEventMutations();

  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<EventFormSchema>({
    resolver: zodResolver(eventFormSchema),
    mode: "onBlur",
    defaultValues: {
      price: 0,
      payment: 0,
      startDate: "",
      endDate: "",
    }
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
      <SectionCard
        title="Dados do evento"
        description="Informe data, valores e participantes do atendimento."
      >

        {/*<DevTool control={control}/>*/}
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

            <fieldset className="fieldset">
              <legend className="fieldset-legend">Título</legend>
              <input type="text" className="input" placeholder="Ex: Aula de matemática" {...register("title")} />
              {errors?.title && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.title.message}</p>
              )}
            </fieldset>

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

            <fieldset className="fieldset md:col-span-3">
              <legend className="fieldset-legend">Descrição (opcional)</legend>
              <textarea className="textarea textarea-bordered w-full" placeholder="Observações do atendimento" {...register("description")} />
              {errors?.description && (
                <p className="label text-error"> <TriangleAlert className="w-3 h-3" /> {errors.description.message}</p>
              )}
            </fieldset>
          </div>

          <div className="mt-1 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
            <ButtonLink to="/events" variant="outline">
              Cancelar
            </ButtonLink>
            <Button type="submit" disabled={createEvent.isPending} variant="primary">
              {createEvent.isPending ? "Salvando..." : "Salvar"}
            </Button>
          </div>
        </form>
      </SectionCard>
    </PageLayout>
  );
}
