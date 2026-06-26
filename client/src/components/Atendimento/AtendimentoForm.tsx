import { zodResolver } from "@hookform/resolvers/zod";
import { useMemo, useState } from "react";
import { FormProvider, useForm } from "react-hook-form";

import { Button } from "@/components/Ui/Button.tsx";
import { Field } from "@/components/Forms/Field.tsx";
import { useGetColaboradoresList, useListAlunos, type AtendimentoResponse } from "@/kubb";
import { useAtendimentoMutations } from "../../services/use-atendimento-mutations.ts";
import { atendimentoFormSchema, type AtendimentoFormSchema } from "../../utils/zod/atendimento-form-schema.ts";
import { SelectInput } from "@/components/Forms/SelectInput.tsx";
import { tipoAtendimentoLabels } from "@/utils/constants/atendimento-constants.ts";
import { NumberInput } from "@/components/Forms/NumberInput.tsx";
import { MonetaryInput } from "@/components/Forms/MonetaryInput.tsx";
import { TextareaInput } from "@/components/Forms/TextAreaInput.tsx";
import { formatDateTimeLocal } from "@/utils/date-utils.ts";

function formatEnd(inicio?: string, duration?: number) {
  if (!inicio || !duration) return "";

  const start = new Date(inicio);
  if (Number.isNaN(start.getTime())) return "";

  const end = new Date(start.getTime() + duration * 60 * 60 * 1000);

  return end.toLocaleString("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  });
}

function toDateValue(dateTime?: string) {
  if (!dateTime) return "";
  return dateTime.slice(0, 10);
}

function toTimeValue(dateTime?: string) {
  if (!dateTime) return "";
  return dateTime.slice(11, 16);
}

function composeDateTime(date: string, time: string) {
  if (!date || !time) return "";
  return `${date}T${time}:00`;
}

type AtendimentoFormProps = {
  initialData?: AtendimentoResponse | null;
  onSuccess: () => void;
  onCancel: () => void;
};


export function AtendimentoForm({ initialData, onSuccess, onCancel }: Readonly<AtendimentoFormProps>) {
  const { createAtendimento, updateAtendimento } = useAtendimentoMutations();
  const alunosList = useListAlunos();
  const colaboradoresList = useGetColaboradoresList();

  const alunoOptions = alunosList.data?.map((aluno) => ({ value: aluno.id, label: aluno.nome })) ?? [];
  const colaboradorOptions = colaboradoresList.data?.map((colaborador) => ({ value: colaborador.id, label: colaborador.nome })) ?? [];
  const tipoAtendimentoOptions = Object.entries(tipoAtendimentoLabels).map(([value, label]) => ({ value, label }));
  const initialInicio = initialData?.inicio ? new Date(initialData.inicio) : new Date();

  const isEditMode = !!initialData;
  const methods = useForm<AtendimentoFormSchema>({
    resolver: zodResolver(atendimentoFormSchema),
    mode: "onBlur",
    defaultValues: {
      valor: initialData?.valor,
      repasse: initialData?.repasse,
      inicio: initialData?.inicio ? formatDateTimeLocal(initialData.inicio) : formatDateTimeLocal(initialInicio),
      duracao: initialData?.duracao,
      tipo: initialData?.tipo,
      alunoId: initialData?.alunoId,
      colaboradorId: initialData?.colaboradorId,
      descricao: initialData?.descricao,
    },
  });

  const duracao = methods.watch("duracao");
  const [inicioData, setInicioData] = useState(() => toDateValue(initialData?.inicio ? formatDateTimeLocal(initialData.inicio) : formatDateTimeLocal(initialInicio)));
  const [inicioHora, setInicioHora] = useState(() => toTimeValue(initialData?.inicio ? formatDateTimeLocal(initialData.inicio) : formatDateTimeLocal(initialInicio)));

  const formattedEndTime = useMemo(() => {
    return formatEnd(composeDateTime(inicioData, inicioHora), duracao);
  }, [inicioData, inicioHora, duracao]);

  const onSubmit = methods.handleSubmit((data: AtendimentoFormSchema) => {
    const inicioValue = composeDateTime(inicioData, inicioHora);
    if (!inicioValue) {
      return;
    }

    if (isEditMode && initialData?.id) {
      updateAtendimento.mutate({
          id: initialData.id,
          data: {
            novoInicio: inicioValue,
            duracao: data.duracao,
          },
        },
        { onSuccess },
      );
      return;
    }
    createAtendimento.mutate({ data: { ...data, inicio: inicioValue } }, { onSuccess });
  });

  const isPending = createAtendimento.isPending || updateAtendimento.isPending;

  return (
    <FormProvider {...methods}>
      <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
          <SelectInput name="alunoId" label="Aluno" options={alunoOptions} />
          <SelectInput name="colaboradorId" label="Colaborador" options={colaboradorOptions} />
          <SelectInput name="tipo" label="Tipo" options={tipoAtendimentoOptions} />

          <Field label="Data Início" error={methods.formState.errors.inicio?.message?.toString()}>
            <input
              type="date"
              className="input w-full"
              value={inicioData}
              onChange={(event) => {
                const value = event.target.value;
                setInicioData(value);
              }}
            />
          </Field>

          <Field label="Horário Início" error={methods.formState.errors.inicio?.message?.toString()}>
            <input
              type="time"
              className="input w-full"
              value={inicioHora}
              onChange={(event) => {
                const value = event.target.value;
                setInicioHora(value);
              }}
            />
          </Field>

          <input type="hidden" {...methods.register("inicio")} value={composeDateTime(inicioData, inicioHora)} readOnly />

          <NumberInput name="duracao" label="Duração (horas)" step="0.5" min={0.5} />

          <Field label="Fim">
            <input type="text" disabled className="input w-full" value={formattedEndTime} readOnly />
          </Field>

          <MonetaryInput name="valor" label="Valor do Atendimento" />
          <MonetaryInput name="repasse" label="Repasse p/ Colaborador" />
          <TextareaInput label="Descrição (opcional)" name="descricao" />
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
    </FormProvider>
  );
}
