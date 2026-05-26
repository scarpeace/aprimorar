import { Button } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import type { PageDTODespesaResponseDTO } from "@/kubb";
import { brl, formatDateShortYear } from "@/lib/utils/formatter";
import { SquareArrowOutUpRight } from "lucide-react";
import { ExpenseCategoryBadge } from "./ExpenseCategoryBadge";
import { ToggleExpensePaymentButton } from "./ToggleExpensePaymentButton";
import { useNavigate } from "react-router-dom";


type ExpensesTableProps = {
  expenses?: PageDTODespesaResponseDTO;
  isPending: boolean;
  error: unknown;
};

export function ExpensesTable({
  expenses,
  isPending,
  error,
}: Readonly<ExpensesTableProps>) {
  const navigate = useNavigate();

  if (error) {
    return (
      <ErrorCard
        title="Nao foi possivel carregar as despesas gerais"
        error={error}
      />
    );
  }

  if (isPending) {
    return <LoadingSpinner text="Carregando despesas gerais..." />;
  }


  if (expenses?.content.length === 0) {
    return (
      <EmptyCard
        title="Nenhuma despesa encontrada"
        description="Nao ha despesas gerais registradas para o periodo selecionado."
      />
    );
  }

  return (
    <div className="max-h-120 overflow-y-auto overflow-x-auto rounded-2xl border border-base-300 bg-base-100 shadow-lg">
      <table className="table table-zebra bg-base-100 animate-[fade-up_280ms_ease-out_both]">
        <thead className="sticky top-0 z-10 bg-base-200/95 backdrop-blur">
          <tr>
            <th className="text-left font-semibold text-base-content/80">
              Data
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Descricao
            </th>
            <th className="text-left font-semibold text-base-content/80">
              Categoria
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Valor
            </th>
            <th className="text-right font-semibold text-base-content/80">
              Ações
            </th>
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {expenses?.content.map((expense) => (
            <tr
              key={expense.id ?? `${expense.date}-${expense.description}`}
            >
              <td className="font-medium text-base-content/70">
                {expense.date ? formatDateShortYear(expense.date) : "-"}
              </td>
              <td className="font-bold text-base-content">
                {expense.description ?? "Despesa sem descricao"}
              </td>
              <td>
                <ExpenseCategoryBadge category={expense.category} />
              </td>
              <td
                className={`text-right font-mono font-semibold ${
                  expense.paymentDate ? "text-success" : "text-base-400"
                }`}
                title={expense.paymentDate ? "Paga" : "Pendente"}
              >
                {brl.format(expense.amount ?? 0)}
              </td>
              <td>
                <div className="flex justify-end gap-2">

                    <ToggleExpensePaymentButton
                      iconOnly
                      isPaid={!!expense.paymentDate}
                      expenseId={expense.id!}
                    />
                    <Button
                      type="button"
                      size="sm"
                      variant="ghost"
                      className="btn-square"
                      title="Ver detalhes"
                      aria-label="Ver detalhes"
                      onClick={() => navigate(`/finance/expenses/${expense.id}`)}
                    >
                      <SquareArrowOutUpRight className="h-4 w-4" />
                    </Button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
