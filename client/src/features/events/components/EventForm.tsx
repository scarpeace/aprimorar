import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useForm, Controller } from "react-hook-form";
import { NumericFormat } from "react-number-format";
import { useMemo } from "react";

import { Button } from "@/components/ui/button";
import { DateTimeInput } from "@/components/ui/date-time-input";
import { EmployeeSelectDropdown } from "@/features/employees/components/EmployeeSelectDropdown";
import { StudentSelectDropdown } from "@/features/students/components/StudentSelectDropdown";
import type { EventRequestDTO, EventResponseDTO } from "@/kubb";
import { toInstant } from "@/lib/utils/dateFormater";
import { ContentSelectDropdown } from "./ContentSelectDropdown";
import { eventFormSchema, type EventFormSchema } from "../forms/eventFormSchema";
import { useEventMutations } from "../hooks/use-event-mutations";
import { formatDateTimeLocal } from "@/lib/utils/formatter";

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

export function EventForm({ initialData, onCancel }: EventFormProps) {
  const { createEvent, updateEvent } = useEventMutations();
  const isEditMode = !!initialData;

  const { register, handleSubmit, control, watch, formState: { errors } } = useForm<EventFormSchema>({
    resolver: zodResolver(eventFormSchema),
    mode: "onBlur",
    defaultValues: {
      price: initialData?.price ?? undefined,
      payment: initialData?.payment ?? undefined,
      startDate: formatDateTimeForInput(initialData?.startDate),
      duration: initialData?.duration ?? 1,
      content: initialData?.content ?? undefined,
      studentId: initialData?.studentId ?? "",
      employeeId: initialData?.employeeId ?? "",
      description: initialData?.description ?? "",
    }
  });

  const startDateValue = watch("startDate");
  const durationValue = watch("duration");

  const displayEndTime = useMemo(() => {
    if (!startDateValue || !durationValue) return "";

    const start = new Date(startDateValue);
    if (isNaN(start.getTime())) return "";

    const hoursInMs = durationValue * 60 * 60 * 1000;
    const end = new Date(start.getTime() + hoursInMs);

    return formatDateTimeLocal(end);
  }, [startDateValue, durationValue]);

  const onSubmit = handleSubmit((data: EventFormSchema) => {
    const formattedData: EventRequestDTO = {
      ...data,
      startDate: toInstant(data.startDate),
    } as any;

    if (isEditMode && initialData.eventId) {
      updateEvent.mutate({ id: initialData.eventId, data: formattedData });
    } else {
      createEvent.mutate({ data: formattedData });
    }
  });

  const isPending = createEvent.isPending || updateEvent.isPending;

  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">

        <StudentSelectDropdown
          registration={register("studentId")}
          error={errors.studentId?.message}
          label="Aluno"
          defaultValue={isEditMode ? { id: initialData.studentId, name: initialData.studentName } : undefined}
        />

        <EmployeeSelectDropdown
          registration={register("employeeId")}
          error={errors.employeeId?.message}
          label="Colaborador"
          defaultValue={isEditMode ? { id: initialData.employeeId, name: initialData.employeeName } : undefined}
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
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" /> {errors.startDate.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Duração (horas)</legend>
          <input
            type="number"
            className="input w-full"
            min="0.5"
            step="0.5"
            {...register("duration", { valueAsNumber: true })}
            placeholder="1.0"
          />
          {errors?.duration && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" /> {errors.duration.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Fim (calculado)</legend>
          <input
            type="text"
            className="input w-full bg-base-200"
            value={displayEndTime}
            placeholder="--"
            disabled
          />
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
