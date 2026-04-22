import { useForm, Controller } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { NumericFormat } from "react-number-format";
import { Button } from "@/components/ui/button";
import { TriangleAlert } from "lucide-react";
import { useExpenseMutations } from "../hooks/use-expense-mutations";
import {
  generalExpenseFormSchema,
  type GeneralExpenseFormSchema,
} from "../forms/generalExpenseFormSchema";
import type { GeneralExpenseResponseDTO } from "@/kubb";

interface GeneralExpenseFormProps {
  initialData?: GeneralExpenseResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
}

const categoryLabels: Record<string, string> = {
  CONTAS: "Contas (Água, Luz, etc.)",
  ADMINISTRATIVO: "Administrativo",
  DESPENSA: "Despensa",
  MANUTENCAO: "Manutenção",
  SERVICOS: "Serviços",
  MATERIAIS: "Materiais",
};

export function GeneralExpenseForm({ initialData, onSuccess, onCancel }: GeneralExpenseFormProps) {
  const { createExpense, updateExpense } = useExpenseMutations({ onSuccessCallback: onSuccess });

  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    control,
    formState: { errors },
  } = useForm<GeneralExpenseFormSchema>({
    resolver: zodResolver(generalExpenseFormSchema),
    defaultValues: {
      description: initialData?.description ?? "",
      amount: initialData?.amount ?? undefined,
      date: initialData?.date ?? new Date().toISOString().split("T")[0],
      category: initialData?.category ?? "CONTAS",
    },
    mode: "onBlur",
  });

  const onSubmit = handleSubmit((data: GeneralExpenseFormSchema) => {
    if (isEditMode && initialData.id) {
      updateExpense.mutate({ id: initialData.id, data });
    } else {
      createExpense.mutate({ data });
    }
  });

  const isPending = createExpense.isPending || updateExpense.isPending;

  return (
    <form onSubmit={onSubmit} className="flex flex-col gap-4" autoComplete="off">
      <fieldset className="fieldset">
        <legend className="fieldset-legend">Descrição</legend>
        <input
          type="text"
          className="input w-full"
          {...register("description")}
          placeholder="Ex: Aluguel Abril"
        />
        {errors?.description && (
          <p className="label text-error">
            <TriangleAlert className="w-3 h-3" />
            {errors.description.message}
          </p>
        )}
      </fieldset>

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <fieldset className="fieldset">
          <legend className="fieldset-legend">Valor</legend>
          <Controller
            control={control}
            name="amount"
            render={({ field: { onChange, onBlur, value, ref } }) => (
              <NumericFormat
                getInputRef={ref}
                className="input w-full"
                placeholder="R$ 0,00"
                value={value}
                onBlur={onBlur}
                onValueChange={(values) => {
                  onChange(values.floatValue);
                }}
                thousandSeparator="."
                decimalSeparator=","
                prefix="R$ "
                decimalScale={2}
                fixedDecimalScale
                allowNegative={false}
              />
            )}
          />
          {errors?.amount && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.amount.message}
            </p>
          )}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Data</legend>
          <input
            type="date"
            className="input w-full"
            {...register("date")}
          />
          {errors?.date && (
            <p className="label text-error">
              <TriangleAlert className="w-3 h-3" />
              {errors.date.message}
            </p>
          )}
        </fieldset>
      </div>

      <fieldset className="fieldset">
        <legend className="fieldset-legend">Categoria</legend>
        <select
          className="select select-bordered w-full"
          {...register("category")}
        >
          {Object.entries(categoryLabels).map(([value, label]) => (
            <option key={value} value={value}>
              {label}
            </option>
          ))}
        </select>
        {errors?.category && (
          <p className="label text-error">
            <TriangleAlert className="w-3 h-3" />
            {errors.category.message}
          </p>
        )}
      </fieldset>

      <div className="flex justify-end gap-2 mt-4">
        <Button type="button" variant="ghost" onClick={onCancel}>
          Cancelar
        </Button>
        <Button type="submit" variant="primary" disabled={isPending}>
          {isPending ? "Salvando..." : (isEditMode ? "Atualizar Despesa" : "Salvar Despesa")}
        </Button>
      </div>
    </form>
  );
}
