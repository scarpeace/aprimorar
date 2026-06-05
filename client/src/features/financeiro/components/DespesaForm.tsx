import { zodResolver } from "@hookform/resolvers/zod";
import { TriangleAlert } from "lucide-react";
import { useForm } from "react-hook-form";
import { Button } from "@/components/ui/button";
import {
  despesaRequestDTOCategoryEnum,
  type DespesaRequestDTO,
  type DespesaResponseDTO,
} from "@/kubb";
import { DESPESA_CATEGORY_LABEL } from "../lib/despesa-category-labels";
import { useDespesaMutations } from "../hooks/use-despesa-mutations";
import { despesaFormSchema, type DespesaFormSchema } from "../lib/despesa-form-schema";

type DespesaFormProps = {
  initialData?: DespesaResponseDTO | null;
  onSuccess: () => void;
  onCancel: () => void;
};

const today = () => new Date().toISOString().slice(0, 10);
const formatDateForInput = (value?: string) => value?.slice(0, 10) ?? today();
const toDespesaInstant = (value: string) => new Date(`${value}T00:00:00.000Z`).toISOString();

export function DespesaForm({
  initialData,
  onSuccess,
  onCancel,
}: Readonly<DespesaFormProps>) {
  const { createExpense, updateExpense } = useDespesaMutations();
  const isEditMode = !!initialData;

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<DespesaFormSchema>({
    resolver: zodResolver(despesaFormSchema),
    defaultValues: {
      amount: initialData?.amount ?? 0,
      category: initialData?.category ?? despesaRequestDTOCategoryEnum.CONTAS,
      date: formatDateForInput(initialData?.date),
      description: initialData?.description ?? "",
    },
    mode: "onBlur",
  });

  const onSubmit = handleSubmit((data) => {
    const payload: DespesaRequestDTO = {
      ...data,
      date: toDespesaInstant(data.date),
    };

    if (isEditMode && initialData?.id) {
      updateExpense.mutate(
        { id: initialData.id, data: payload },
        { onSuccess: () => onSuccess() },
      );
      return;
    }

    createExpense.mutate({ data: payload }, { onSuccess: () => onSuccess() });
  });

  const isPending = createExpense.isPending || updateExpense.isPending;

  return (
    <form className="flex flex-col gap-3" onSubmit={onSubmit} autoComplete="off">
      <div className="grid grid-cols-1 gap-4 md:grid-cols-3 md:gap-5">
        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Descricao</legend>
          <input
            type="text"
            className="input w-full"
            placeholder="Ex: Conta de luz"
            {...register("description")}
          />
          {errors.description ? (
            <p className="label text-error">
              <TriangleAlert className="h-3 w-3" />
              {errors.description.message}
            </p>
          ) : null}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Categoria</legend>
          <select className="select select-bordered w-full" {...register("category")}>
            {Object.values(despesaRequestDTOCategoryEnum).map((category) => (
              <option key={category} value={category}>
                {DESPESA_CATEGORY_LABEL[category] ?? category}
              </option>
            ))}
          </select>
          {errors.category ? (
            <p className="label text-error">
              <TriangleAlert className="h-3 w-3" />
              {errors.category.message}
            </p>
          ) : null}
        </fieldset>

        <fieldset className="fieldset">
          <legend className="fieldset-legend">Data</legend>
          <input type="date" className="input w-full" {...register("date")} />
          {errors.date ? (
            <p className="label text-error">
              <TriangleAlert className="h-3 w-3" />
              {errors.date.message}
            </p>
          ) : null}
        </fieldset>

        <fieldset className="fieldset md:col-span-2">
          <legend className="fieldset-legend">Valor</legend>
          <label className="input w-full">
            <span className="label text-base-content/60">R$</span>
            <input
              type="number"
              min="0"
              step="0.01"
              placeholder="0,00"
              {...register("amount", { valueAsNumber: true })}
            />
          </label>
          {errors.amount ? (
            <p className="label text-error">
              <TriangleAlert className="h-3 w-3" />
              {errors.amount.message}
            </p>
          ) : null}
        </fieldset>
      </div>

      <div className="mt-4 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end">
        <Button type="button" variant="ghost" onClick={onCancel} disabled={isPending}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isPending} variant="primary">
          {isPending
            ? "Salvando..."
            : isEditMode
              ? "Atualizar Despesa"
              : "Salvar Despesa"}
        </Button>
      </div>
    </form>
  );
}
