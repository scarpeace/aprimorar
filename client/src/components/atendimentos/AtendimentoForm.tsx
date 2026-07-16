"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import Link from "next/link";
import { FormProvider, useForm, useWatch } from "react-hook-form";
import type { AtendimentoResponse } from "@/lib/api/generated/types/AtendimentoResponse";
import { useListAlunos } from "@/lib/api/generated/hooks/aluno/useListAlunos";
import { useGetColaboradoresList } from "@/lib/api/generated/hooks/colaborador/useGetColaboradoresList";
import { Button } from "@/components/ui/Button";
import { Field } from "@/components/ui/forms/Field";
import { MonetaryInput } from "@/components/ui/forms/MonetaryInput";
import { SelectInput } from "@/components/ui/forms/SelectInput";
import { TextInput } from "@/components/ui/forms/TextInput";
import { useAtendimentoMutations } from "@/hooks/use-atendimento-mutations";
import { atendimentoTipoOptions } from "@/lib/constants/atendimento-constants";
import { addHoursToDateTimeLocal, formatDateTimeLocal, getDurationInHours } from "@/lib/utils/date-utils";
import { atendimentoFormSchema, type AtendimentoFormInput } from "@/lib/validators/atendimento-form-schema";

const CREATE_TIPO_OPTIONS = atendimentoTipoOptions.filter((option) => option.value !== "");

function toApiDateTime(value: string) {
  return value ? `${value}:00` : "";
}

type AtendimentoFormProps = {
  initialData?: AtendimentoResponse;
  onSuccess: () => void;
  onCancel: () => void;
};

export function AtendimentoForm({ initialData, onSuccess, onCancel }: Readonly<AtendimentoFormProps>) {
  const { createAtendimento, updateAtendimento } = useAtendimentoMutations();
  const isEditMode = !!initialData;
  const alunos = useListAlunos();
  const colaboradores = useGetColaboradoresList();

  const methods = useForm<AtendimentoFormInput>({
    resolver: zodResolver(atendimentoFormSchema),
    mode: "onBlur",
    defaultValues: {
      alunoId: initialData?.alunoId ?? "",
      colaboradorId: initialData?.colaboradorId ?? "",
      tipo: initialData?.tipo ?? "AULA",
      dataHoraInicio: formatDateTimeLocal(initialData?.dataHoraInicio ?? new Date()),
      duracao: initialData ? getDurationInHours(initialData.dataHoraInicio, initialData.dataHoraFim) : 1,
      pagamentoAluno: initialData?.pagamentoAluno,
      repasseColaborador: initialData?.repasseColaborador,
    },
  });

  const isPending = createAtendimento.isPending || updateAtendimento.isPending;
  const [dataHoraInicio, duracao] = useWatch({
    control: methods.control,
    name: ["dataHoraInicio", "duracao"],
  });
  const duracaoValue = Number(duracao);
  const dataHoraFimCalculada = addHoursToDateTimeLocal(dataHoraInicio, Number.isFinite(duracaoValue) ? duracaoValue : undefined);

  const alunoOptions =
    alunos.data?.map((item) => ({
      value: item.id,
      label: item.nome,
    })) ?? [];

  const colaboradorOptions =
    colaboradores.data?.map((item) => ({
      value: item.id,
      label: item.nome,
    })) ?? [];

  const onSubmit = methods.handleSubmit((data) => {
    if (!dataHoraFimCalculada) {
      methods.setError("dataHoraInicio", { message: "Não foi possível calcular o fim do atendimento" });
      return;
    }

    const payload = {
      tipo: data.tipo,
      dataHoraInicio: toApiDateTime(data.dataHoraInicio),
      dataHoraFim: toApiDateTime(dataHoraFimCalculada),
      pagamentoAluno: data.pagamentoAluno,
      repasseColaborador: data.repasseColaborador,
      alunoId: data.alunoId,
      colaboradorId: data.colaboradorId,
    };

    if (isEditMode && initialData) {
      updateAtendimento.mutate(
        {
          id: initialData.id,
          data: payload,
        },
        {
          onSuccess,
        },
      );
      return;
    }

    createAtendimento.mutate(
      {
        data: payload,
      },
      {
        onSuccess,
      },
    );
  });

  return (
    <FormProvider {...methods}>
      <form className="flex flex-col gap-6" autoComplete="off" onSubmit={onSubmit}>
        <section className="space-y-4">
          <div>
            <h4 className="text-base font-bold text-base-content">Dados do atendimento</h4>
            <p className="text-sm text-base-content/60">Defina participante, horário e valores do atendimento.</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <div>
              <SelectInput
                name="alunoId"
                label="Aluno"
                options={alunoOptions}
                placeholder="Selecione um aluno"
                disabled={isPending || alunos.isLoading}
              />

              <p className="mt-2 text-sm text-base-content/60">
                Não encontrou o aluno?{" "}
                <Link className="link link-primary font-medium" href="/alunos">
                  Cadastre primeiro em alunos
                </Link>
                .
              </p>
            </div>

            <div>
              <SelectInput
                name="colaboradorId"
                label="Colaborador"
                options={colaboradorOptions}
                placeholder="Selecione um colaborador"
                disabled={isPending || colaboradores.isLoading}
              />

              <p className="mt-2 text-sm text-base-content/60">
                Não encontrou o colaborador?{" "}
                <Link className="link link-primary font-medium" href="/colaboradores">
                  Cadastre primeiro em colaboradores
                </Link>
                .
              </p>
            </div>

            <SelectInput name="tipo" label="Tipo" options={CREATE_TIPO_OPTIONS} disabled={isPending} />
            <TextInput name="dataHoraInicio" type="datetime-local" label="Início" disabled={isPending} />
            <TextInput
              name="duracao"
              type="number"
              min="0.5"
              step="0.5"
              label="Duração (horas)"
              helperText="Use 0.5 para 30 minutos, 1 para 1 hora, 1.5 para 1h30."
              disabled={isPending}
            />

            <Field label="Fim calculado" helperText="Campo calculado automaticamente a partir do início e duração.">
              <input type="datetime-local" value={dataHoraFimCalculada} disabled className="input input-bordered w-full" />
            </Field>

            <MonetaryInput name="pagamentoAluno" label="Valor pago pelo aluno" disabled={isPending} />
            <MonetaryInput name="repasseColaborador" label="Repasse ao colaborador" disabled={isPending} />
          </div>
        </section>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Button type="button" variant="outline" disabled={isPending} onClick={onCancel}>
            Cancelar
          </Button>
          <Button type="submit" disabled={isPending}>
            {isPending ? "Salvando..." : isEditMode ? "Salvar alterações" : "Cadastrar atendimento"}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
