import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useMemo } from "react";
import { Controller, useForm, useWatch } from "react-hook-form";
import { NumericFormat } from "react-number-format";

import { Button } from "@/components/ui/button";
import { DateTimeInput } from "@/components/ui/date-time-input";
import { AlunoSelectDropdown } from "@/features/alunos/components/AlunoSelectDropdown";
import { ColaboradorSelectDropdown } from "@/features/colaboradores/components/ColaboradorSelectDropdown";
import type { AtendimentoRequestDTO, AtendimentoResponseDTO } from "@/kubb";
import { toInstant } from "@/lib/utils/date-utils";
import { useAtendimentoMutations } from "../hooks/use-atendimento-mutations";
import { atendimentoFormSchema, type AtendimentoFormSchema } from "../lib/atendimento-form-schema";
import { ContentSelectDropdown } from "./ContentSelectDropdown";

function formatDateTimeForInput(dateTimeStr?: string) {
  if (!dateTimeStr) {
    return "";
  }

  const date = new Date(dateTimeStr);
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 16);
}

type AtendimentoFormProps = {
  initialData?: AtendimentoResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
};

function FieldErrorMessage({ message }: Readonly<{ message?: string }>) {
  if (!message) {
    return null;
  }

  return (
    <p className="label text-error">
      <TriangleAlert className="w-3 h-3" />
      {message}
    </p>
  );
}

export function AtendimentoForm({ initialData, onCancel, onSuccess }: Readonly<AtendimentoFormProps>) {
  const { createAtendimento, updateAtendimento } = useAtendimentoMutations();
  const isEditMode = !!initialData;

  const { register, handleSubmit, control, formState: { errors } } = useForm<AtendimentoFormSchema>({
    resolver: zodResolver(atendimentoFormSchema),
    mode: "onBlur",
    defaultValues: {
      valor: initialData?.valor ?? undefined,
      repasse: initialData?.repasse ?? undefined,
      inicio: formatDateTimeForInput(initialData?.inicio),
      duracao: initialData?.duracao ?? 1,
      tipo: initialData?.tipo ?? undefined,
      alunoId: initialData?.alunoId ?? "",
      colaboradorId: initialData?.colaboradorId ?? "",
      descricao: initialData?.descricao ?? "",
    },
  });

  const inicioValue = useWatch({ control, name: "inicio" });
  const duracaoValue = useWatch({ control, name: "duracao" });

  const formattedEndTime = useMemo(() => {
    if (!inicioValue || !duracaoValue) {
      return "";
    }

    const start = new Date(inicioValue);
    if (Number.isNaN(start.getTime())) {
      return "";
    }

    const hoursInMs = duracaoValue * 60 * 60 * 1000;
    const end = new Date(start.getTime() + hoursInMs);

    return new Intl.DateTimeFormat("pt-BR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    }).format(end);
  }, [inicioValue, duracaoValue]);

  const onSubmit = handleSubmit((data) => {
    const formattedData: AtendimentoRequestDTO = {
      descricao: data.descricao,
      tipo: data.tipo,
      inicio: toInstant(data.inicio),
      duracao: data.duracao,
      valor: data.valor,
      repasse: data.repasse,
      alunoId: data.alunoId,
      colaboradorId: data.colaboradorId,
    };

    if (isEditMode && initialData?.id) {
      updateAtendimento.mutate(
        { id: initialData.id, data: formattedData },
        { onSuccess },
      );
      return;
    }

    createAtendimento.mutate({ data: formattedData }, { onSuccess });
  });

  const isPending = createAtendimento.isPending || updateAtendimento.isPending;

  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
        <AlunoSelectDropdown
          registration={register("alunoId")}
          error={errors.alunoId?.message}
          label="Aluno"
          defaultValue={isEditMode && initialData ? { id: initialData.alunoId, name: initialData.alunoNome } : undefined}
        />

        <ColaboradorSelectDropdown
          registration={register("colaboradorId")}
          error={errors.colaboradorId?.message}
          label="Colaborador"
          defaultValue={isEditMode && initialData ? { id: initialData.colaboradorId, name: initialData.colaboradorNome } : undefined}
        />

        <ContentSelectDropdown
          label="Tipo"
          registration={register("tipo")}
          error={errors.tipo?.message}
        />

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Data Início</legend>
          <DateTimeInput control={control} name="inicio" placeholderText="Início" />
          <FieldErrorMessage message={errors.inicio?.message} />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Duração (horas)</legend>
          <input
            type="number"
            className="input w-full"
            min="0.5"
            step="0.5"
            {...register("duracao", { valueAsNumber: true })}
            placeholder="1.0"
          />
          <FieldErrorMessage message={errors.duracao?.message} />
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Fim (calculado)</legend>
          <input
            type="text"
            className="input w-full bg-base-200"
            value={formattedEndTime}
            placeholder="--"
            disabled
          />
        </fieldset>

        <div className="flex flex-col sm:flex-row md:col-span-3 gap-3">
          <fieldset className="fieldset w-full">
            <legend className="fieldset-legend">Valor do atendimento</legend>
            <Controller
              control={control}
              name="valor"
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
            <FieldErrorMessage message={errors.valor?.message} />
          </fieldset>

          <fieldset className="fieldset w-full">
            <legend className="fieldset-legend">Repasse p/ Colaborador</legend>
            <Controller
              control={control}
              name="repasse"
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
            <FieldErrorMessage message={errors.repasse?.message} />
          </fieldset>
        </div>

        <fieldset className="fieldset md:col-span-3">
          <legend className="fieldset-legend">Descrição (opcional)</legend>
          <textarea className="textarea textarea-bordered w-full" placeholder="Observações do atendimento" {...register("descricao")} />
          <FieldErrorMessage message={errors.descricao?.message} />
        </fieldset>
      </div>

      <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel} disabled={isPending}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isPending} variant="primary">
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Atendimento" : "Salvar Atendimento")}
        </Button>
      </div>
    </form>
  );
}
