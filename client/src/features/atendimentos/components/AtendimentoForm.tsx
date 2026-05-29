import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useForm, Controller, useWatch } from "react-hook-form";
import { NumericFormat } from "react-number-format";
import { useMemo } from "react";

import { Button } from "@/components/ui/button";
import { DateTimeInput } from "@/components/ui/date-time-input";
import { ColaboradorSelectDropdown } from "@/features/colaboradores/components/ColaboradorSelectDropdown";
import { AlunoSelectDropdown } from "@/features/alunos/components/AlunoSelectDropdown";
import type { AtendimentoRequestDTO, AtendimentoResponseDTO } from "@/kubb";
import { toInstant } from "@/lib/utils/date-utils";
import { ContentSelectDropdown } from "./ContentSelectDropdown";
import { atendimentoFormSchema, type AtendimentoFormSchema } from "../lib/appointmentFormSchema.tsx";
import { useAtendimentoMutations } from "../hooks/use-atendimento-mutations";
import { formatDateTimeLocal } from "@/lib/utils/formatter";

function formatDateTimeForInput(dateTimeStr?: string) {
  if (!dateTimeStr) return "";
  const date = new Date(dateTimeStr);
  const offset = date.getTimezoneOffset() * 60000;
  const localISOTime = new Date(date.getTime() - offset).toISOString().slice(0, 16);
  return localISOTime;
}

interface AtendimentoFormProps {
  initialData?: AtendimentoResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

export function AtendimentoForm({ initialData, onCancel, onSuccess }: AtendimentoFormProps) {
  const { createAppointment, updateAppointment } = useAtendimentoMutations();
  const isEditMode = !!initialData;

  const { register, handleSubmit, control, formState: { errors } } = useForm<AtendimentoFormSchema>({
    resolver: zodResolver(atendimentoFormSchema),
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

  const startDateValue = useWatch({ control, name: "startDate" });
  const durationValue = useWatch({ control, name: "duration" });

  const displayEndTime = useMemo(() => {
    if (!startDateValue || !durationValue) return "";

    const start = new Date(startDateValue);
    if (isNaN(start.getTime())) return "";

    const hoursInMs = durationValue * 60 * 60 * 1000;
    const end = new Date(start.getTime() + hoursInMs);

    return formatDateTimeLocal(end);
  }, [startDateValue, durationValue]);

  const onSubmit = handleSubmit((data: AtendimentoFormSchema) => {
    const formattedData: AtendimentoRequestDTO = {
      description: data.description,
      content: data.content,
      startDate: toInstant(data.startDate),
      duration: data.duration,
      price: data.price,
      payment: data.payment,
      studentId: data.studentId,
      employeeId: data.employeeId,
    };

    if (isEditMode && initialData.id) {
      updateAppointment.mutate({ id: initialData.id, data: formattedData });
    } else {
      createAppointment.mutate({ data: formattedData });
    }
    onSuccess()
  });

  const isPending = createAppointment.isPending || updateAppointment.isPending;

  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">

        <AlunoSelectDropdown
          registration={register("studentId")}
          error={errors.studentId?.message}
          label="Aluno"
          defaultValue={isEditMode ? { id: initialData.studentId, name: initialData.studentName } : undefined}
        />

        <ColaboradorSelectDropdown
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
