import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useForm, Controller } from "react-hook-form";
import { NumericFormat } from "react-number-format";

import { Button } from "@/components/ui/button";
import { DateTimeInput } from "@/components/ui/date-time-input";
import { EmployeeSelectDropdown } from "@/features/employees/components/EmployeeSelectDropdown";
import { StudentSelectDropdown } from "@/features/students/components/StudentSelectDropdown";
import { toInstant } from "@/lib/utils/dateFormater";
import { ContentSelectDropdown } from "./ContentSelectDropdown";
import { StatusSelectDropdown } from "./StatusSelectDropdown";
import type { EventResponseDTO } from "@/kubb";
import { eventFormSchema, type EventFormSchema } from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";

function formatDateTimeForInput(dateTimeStr?: string) {
  if (!dateTimeStr) return "";
  // We just remove the "Z" or milliseconds for datetime-local input
  const date = new Date(dateTimeStr);
  const offset = date.getTimezoneOffset() * 60000;
  const localISOTime = new Date(date.getTime() - offset).toISOString().slice(0, 16);
  return localISOTime;
}

interface EventFormProps {
  initialData?: EventResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function EventForm({ initialData, onSuccess, onCancel }: EventFormProps) {
  const { createEvent, updateEvent } = useEventMutations({ onSuccessCallback: onSuccess });
  const isEditMode = !!initialData;

  const { register, handleSubmit, control, formState: { errors } } = useForm<EventFormSchema>({
    resolver: zodResolver(eventFormSchema),
    mode: "onBlur",
    defaultValues: {
      price: initialData?.price ?? undefined,
      payment: initialData?.payment ?? undefined,
      startDate: formatDateTimeForInput(initialData?.startDate),
      endDate: formatDateTimeForInput(initialData?.endDate),
      status: initialData?.status ?? "SCHEDULED",
      content: initialData?.content ?? undefined,
      studentId: initialData?.studentId ?? "",
      employeeId: initialData?.employeeId ?? "",
      description: initialData?.description ?? "",
    }
  });

  const onSubmit = handleSubmit((data: EventFormSchema) => {
    const formattedData = {
      ...data,
      startDate: toInstant(data.startDate),
      endDate: toInstant(data.endDate),
    };

    if (isEditMode && initialData.eventId) {
      updateEvent.mutate({ eventId: initialData.eventId, data: formattedData });
    } else {
      createEvent.mutate({ data: formattedData });
    }
  });

  const isPending = createEvent.isPending || updateEvent.isPending;

  return (
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
            <p className="label text-error"> 
              <TriangleAlert className="w-3 h-3" /> {errors.startDate.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Data Fim</legend>
          <DateTimeInput control={control} name="endDate" placeholderText="Fim" />
          {errors?.endDate && (
            <p className="label text-error"> 
              <TriangleAlert className="w-3 h-3" /> {errors.endDate.message}
            </p>
          )}
        </fieldset>

        <div className="flex flex-col sm:flex-row md:col-span-3 gap-3">
          <fieldset className="fieldset w-full">
            <legend className="fieldset-legend">Preço (receita)</legend>
            <Controller
              control={control}
              name="price"
              render={({ field: { onChange, onBlur, value, ref } }) => (
                <NumericFormat
                  getInputRef={ref}
                  className="input w-full"
                  placeholder="R$ 0,00"
                  value={value}
                  onBlur={onBlur}
                  onValueChange={(values) => onChange(values.floatValue)}
                  thousandSeparator="."
                  decimalSeparator=","
                  prefix="R$ "
                  decimalScale={2}
                  fixedDecimalScale
                  allowNegative={false}
                />
              )}
            />
            {errors?.price && (
              <p className="label text-error"> 
                <TriangleAlert className="w-3 h-3" /> {errors.price.message}
              </p>
            )}
          </fieldset>

          <fieldset className="fieldset w-full">
            <legend className="fieldset-legend">Pagamento (custo)</legend>
            <Controller
              control={control}
              name="payment"
              render={({ field: { onChange, onBlur, value, ref } }) => (
                <NumericFormat
                  getInputRef={ref}
                  className="input w-full"
                  placeholder="R$ 0,00"
                  value={value}
                  onBlur={onBlur}
                  onValueChange={(values) => onChange(values.floatValue)}
                  thousandSeparator="."
                  decimalSeparator=","
                  prefix="R$ "
                  decimalScale={2}
                  fixedDecimalScale
                  allowNegative={false}
                />
              )}
            />
            {errors?.payment && (
              <p className="label text-error"> 
                <TriangleAlert className="w-3 h-3" /> {errors.payment.message}
              </p>
            )}
          </fieldset>
        </div>

        <fieldset className="fieldset md:col-span-3">
          <legend className="fieldset-legend">Descrição (opcional)</legend>
          <textarea className="textarea textarea-bordered w-full" placeholder="Observações do atendimento" {...register("description")} />
          {errors?.description && (
            <p className="label text-error"> 
              <TriangleAlert className="w-3 h-3" /> {errors.description.message}
            </p>
          )}
        </fieldset>
      </div>

      <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isPending} variant="primary">
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Atendimento" : "Salvar Atendimento")}
        </Button>
      </div>
    </form>
  );
}
