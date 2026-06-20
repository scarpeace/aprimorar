import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useMemo } from "react";
import { Controller, useForm, useWatch } from "react-hook-form";
import { NumericFormat } from "react-number-format";

import { Button } from "@/components/ui/button";
import { DateTimeInput } from "@/components/ui/date-time-input";
import { AlunoSelectDropdown } from "@/features/alunos/components/AlunoSelectDropdown";
import { ColaboradorSelectDropdown } from "@/features/colaboradores/components/ColaboradorSelectDropdown";
import { useGetColaboradoresList, useListAlunos, type AtendimentoRequest, type AtendimentoResponse } from "@/kubb";
import { toInstant } from "@/lib/utils/date-utils";
import { useAtendimentoMutations } from "../hooks/use-atendimento-mutations";
import { atendimentoFormSchema, type AtendimentoFormSchema } from "../lib/atendimento-form-schema";
import { ContentSelectDropdown } from "./ContentSelectDropdown";
import { SelectInput } from "@/components/forms/SelectInput";
import { tipoAtendimentoLabels } from "../lib/tipo-atendimento-labels";
import { DateInput } from "@/components/forms/DateInput";
import { TextInput } from "@/components/forms/TextInput";
import { NumberInput } from "@/components/forms/NumberInput";
import { TextareaInput } from "@/components/forms/TextAreaInput";

function formatDateTimeForInput(dateTimeStr?: string) {
  if (!dateTimeStr) {
    return "";
  }

  const date = new Date(dateTimeStr);
  const offset = date.getTimezoneOffset() * 60000;
  return new Date(date.getTime() - offset).toISOString().slice(0, 16);
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

  const alunoOptions = alunosList.data?.map((aluno) => ({ value: aluno.id, label: aluno.nome }))?? [];
  const colaboradorOptions = colaboradoresList.data?.map((colaborador) => ({ value: colaborador.id, label: colaborador.nome })) ?? [];
  const tipoAtendimentoOptions = Object.entries(tipoAtendimentoLabels).map(([value, label]) => ({ value, label }));

  const isEditMode = !!initialData;
  const methods = useForm<AtendimentoFormSchema>({
    resolver: zodResolver(atendimentoFormSchema),
    mode: "onBlur",
    defaultValues: {
      valor: initialData?.valor ,
      repasse: initialData?.repasse,
      inicio: formatDateTimeForInput(initialData?.inicio),
      duracao: initialData?.duracao,
      tipo: initialData?.tipo,
      alunoId: initialData?.alunoId,
      colaboradorId: initialData?.colaboradorId,
      descricao: initialData?.descricao,
    },
  });

  const { inicio, duracao } = methods.watch();
  const formattedEndTime = useMemo(() => {
    if (!inicio || !duracao) {
      return "";
    }

    const start = new Date(inicio);
    if (Number.isNaN(start.getTime())) {
      return "";
    }

    const hoursInMs = duracao * 60 * 60 * 1000;
    return new Date(start.getTime() + hoursInMs).toDateString();

    // return new Intl.DateTimeFormat("pt-BR", {
    //   day: "2-digit",
    //   month: "2-digit",
    //   year: "numeric",
    //   hour: "2-digit",
    //   minute: "2-digit",
    // }).format(end);
  }, [inicio, duracao]);

  const onSubmit = methods.handleSubmit((data: AtendimentoFormSchema) => {
    if (isEditMode && initialData?.id) {
      updateAtendimento.mutate({
          id: initialData.id,
          data: {
            novoInicio: data.inicio,
            duracao: data.duracao,
          },
        },
        { onSuccess },
      );
      return;
    }
    createAtendimento.mutate({ data }, { onSuccess });
  });

  const isPending = createAtendimento.isPending || updateAtendimento.isPending;

  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">

        <SelectInput name="alunoId" label="Aluno" options={alunoOptions} />
        <SelectInput name="colaboradorId" label="Colaborador" options={colaboradorOptions} />
        <SelectInput name="tipo" label="Tipo" options={tipoAtendimentoOptions} />

        <DateInput
          dateTime={true}
          name="inicio"
          placeholder="Início"
          label="Data Início"
          registration={methods.register("inicio")}
          error={methods.formState.errors.inicio?.message}
        />
        <input type="number" name="duracao" placeholder="Duração (horas)" />

        <TextInput name="duracao" label="Duração (horas)" />

        <TextInput label="Fim" name={"fim"} disabled={true} value={formattedEndTime} />
        <NumberInput label="Valor do Atendimento" name={"valor"} />
        <NumberInput label="Repasse p/ Colaborador" name={"repasse"} />
        <TextareaInput label="Descrição (opcional)" name={"descricao"} />
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
