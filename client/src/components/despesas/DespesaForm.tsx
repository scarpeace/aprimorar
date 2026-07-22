"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { FormProvider, useForm } from "react-hook-form";
import { Button } from "@/components/ui/Button";
import { DateInput } from "@/components/ui/forms/DateInput";
import { MonetaryInput } from "@/components/ui/forms/MonetaryInput";
import { SelectInput } from "@/components/ui/forms/SelectInput";
import { TextAreaInput } from "@/components/ui/forms/TextAreaInput";
import { TextInput } from "@/components/ui/forms/TextInput";
import { useDespesaMutations } from "@/hooks/use-despesa-mutations";
import {
  createCategoriaDespesaOptions,
  createFormaPagamentoDespesaOptions,
} from "@/lib/constants/despesa-constants";
import type { DespesaResponse } from "@/lib/api/generated/types/DespesaResponse";
import { despesaFormSchema, type DespesaFormData } from "@/lib/validators/despesa-form-schema";

type DespesaFormProps = {
  initialData?: DespesaResponse;
  onSuccess: () => void;
  onCancel: () => void;
};

export function DespesaForm({ initialData, onSuccess, onCancel }: Readonly<DespesaFormProps>) {
  const { createDespesa, updateDespesa } = useDespesaMutations();
  const isEditMode = !!initialData?.id;

  const methods = useForm<DespesaFormData>({
    resolver: zodResolver(despesaFormSchema),
    mode: "onBlur",
    defaultValues: {
      titulo: initialData?.titulo ?? "",
      categoria: initialData?.categoria ?? "CONTAS",
      valor: initialData?.valor,
      dataPagamento: initialData?.dataPagamento ?? "",
      formaPagamento: initialData?.formaPagamento ?? "PIX",
      descricao: initialData?.descricao ?? "",
    },
  });

  const isPending = createDespesa.isPending || updateDespesa.isPending;

  const onSubmit = methods.handleSubmit((data) => {
    const payload = {
      ...data,
      dataPagamento: data.dataPagamento || null,
      descricao: data.descricao?.trim() || undefined,
    };

    if (isEditMode && initialData?.id) {
      updateDespesa.mutate(
        { despesaId: initialData.id, data: payload },
        {
          onSuccess,
        },
      );
      return;
    }

    createDespesa.mutate(
      { data: payload },
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
            <h4 className="text-base font-bold text-base-content">Dados da despesa</h4>
            <p className="text-sm text-base-content/60">Registre título, categoria, pagamento e valor.</p>
          </div>

          <div className="grid gap-4 md:grid-cols-2">
            <TextInput name="titulo" label="Título" disabled={isPending} />
            <SelectInput name="categoria" label="Categoria" options={createCategoriaDespesaOptions} disabled={isPending} />
            <MonetaryInput name="valor" label="Valor" disabled={isPending} />
            <DateInput name="dataPagamento" label="Data do pagamento" disabled={isPending} />
            <SelectInput
              name="formaPagamento"
              label="Forma de pagamento"
              options={createFormaPagamentoDespesaOptions}
              disabled={isPending}
            />
            <TextAreaInput name="descricao" label="Descrição" rows={3} disabled={isPending} className="md:col-span-2" />
          </div>
        </section>

        <div className="flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
          <Button type="button" variant="outline" disabled={isPending} onClick={onCancel}>
            Cancelar
          </Button>
          <Button type="submit" disabled={isPending}>
            {isPending ? "Salvando..." : isEditMode ? "Salvar alterações" : "Cadastrar despesa"}
          </Button>
        </div>
      </form>
    </FormProvider>
  );
}
