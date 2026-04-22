import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Button } from "@/components/ui/button";
import { useCreateGeneralExpense } from "@/kubb/hooks/general-expenses/useCreateGeneralExpense";
import { getGeneralExpensesQueryKey } from "@/kubb/hooks/general-expenses/useGetGeneralExpenses";
import { generalExpenseRequestDTOCategoryEnum } from "@/kubb/types/GeneralExpenseRequestDTO";
import { toast } from "sonner";
import { useQueryClient } from "@tanstack/react-query";
import { TriangleAlert } from "lucide-react";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";

const expenseFormSchema = z.object({
  description: z.string().min(1, "A descrição é obrigatória"),
  amount: z.string().min(1, "O valor é obrigatório"),
  date: z.string().min(1, "A data é obrigatória"),
  category: z.nativeEnum(generalExpenseRequestDTOCategoryEnum),
});

type ExpenseFormValues = z.infer<typeof expenseFormSchema>;

interface GeneralExpenseFormProps {
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

export function GeneralExpenseForm({ onSuccess, onCancel }: GeneralExpenseFormProps) {
  const queryClient = useQueryClient();
  const { mutate: createExpense, isPending } = useCreateGeneralExpense({
    mutation: {
      onSuccess: () => {
        toast.success("Despesa cadastrada com sucesso");
        queryClient.invalidateQueries({ queryKey: getGeneralExpensesQueryKey() });
        onSuccess();
      },
      onError: (error) => {
        toast.error(getFriendlyErrorMessage(error) || "Erro ao cadastrar despesa");
      },
    },
  });

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm<ExpenseFormValues>({
    resolver: zodResolver(expenseFormSchema),
    defaultValues: {
      date: new Date().toISOString().split("T")[0],
      category: "CONTAS",
    },
    mode: "onBlur",
  });

  const onSubmit = (values: ExpenseFormValues) => {
    // Converte valor formatado (ex: 1.234,56) para number (ex: 1234.56)
    const numericAmount = parseFloat(
      values.amount.replace(/\./g, "").replace(",", ".")
    );

    createExpense({
      data: {
        ...values,
        amount: numericAmount,
      },
    });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="flex flex-col gap-4" autoComplete="off">
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
          <legend className="fieldset-legend">Valor (R$)</legend>
          <input
            type="text"
            className="input w-full"
            placeholder="0,00"
            {...register("amount")}
            onChange={(e) => {
              // Máscara simples de moeda
              let value = e.target.value.replace(/\D/g, "");
              value = (Number(value) / 100).toLocaleString("pt-BR", {
                minimumFractionDigits: 2,
              });
              setValue("amount", value);
            }}
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
          {isPending ? "Salvando..." : "Salvar Despesa"}
        </Button>
      </div>
    </form>
  );
}
