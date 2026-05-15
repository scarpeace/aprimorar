import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import type { ExpenseResponseDTO, PageDTOExpenseResponseDTO } from "@/kubb";
import { brl, formatDateShortYear } from "@/lib/utils/formatter";
import { ExpenseCategoryBadge } from "./ExpenseCategoryBadge";

type ExpensesTableProps = {
  expenses?: PageDTOExpenseResponseDTO;
  isPending: boolean;
  error: unknown;
  onNavigate?: (expense: ExpenseResponseDTO) => void;
};

export function ExpensesTable({
  expenses,
  isPending,
  error,
  onNavigate,
}: Readonly<ExpensesTableProps>) {
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

  const rows = expenses?.content ?? [];

  if (rows.length === 0) {
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
          </tr>
        </thead>

        <tbody className="whitespace-nowrap">
          {rows.map((expense) => (
            <tr
              key={expense.id ?? `${expense.date}-${expense.description}`}
              className={onNavigate ? "cursor-pointer" : undefined}
              role={onNavigate ? "button" : undefined}
              tabIndex={onNavigate ? 0 : undefined}
              onClick={() => onNavigate?.(expense)}
              onKeyDown={(event) => {
                if (!onNavigate) return;
                if (event.key === "Enter" || event.key === " ") {
                  event.preventDefault();
                  onNavigate(expense);
                }
              }}
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
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
