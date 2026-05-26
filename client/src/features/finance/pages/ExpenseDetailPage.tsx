import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { DeleteConfirmationModal } from "@/components/ui/delete-confirmation-modal";
import { PageLayout } from "@/components/layout/PageLayout";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { useGetDespesaById } from "@/kubb";
import { brl, formatDateShortYear } from "@/lib/utils/formatter";
import { Edit, ReceiptText, Trash2 } from "lucide-react";
import { ExpenseForm } from "../components/ExpenseForm";
import { ToggleExpensePaymentButton } from "../components/ToggleExpensePaymentButton";
import { useExpenseMutations } from "../hooks/use-expense-mutation";
import { EXPENSE_CATEGORY_LABEL } from "../lib/expense-category-labels";

function DetailItem({
  label,
  value,
  className = "",
}: Readonly<{ label: string; value: string; className?: string }>) {
  return (
    <div className="rounded-2xl border border-base-300 bg-base-200/40 p-4">
      <p className="text-xs font-semibold uppercase tracking-[0.18em] text-base-content/50">
        {label}
      </p>
      <p className={`mt-2 text-lg font-bold text-base-content ${className}`}>
        {value}
      </p>
    </div>
  );
}

export function ExpenseDetailPage() {
  const { id } = useParams<{ id: string }>();
  const expenseId = id ?? "";
  const navigate = useNavigate();
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [isDeleteOpen, setIsDeleteOpen] = useState(false);
  const expenseQuery = useGetDespesaById(expenseId);
  const { deleteExpense } = useExpenseMutations();

  const headerProps = {
    description: "Veja e gerencie os dados da despesa selecionada.",
    title: "Detalhes da despesa",
    Icon: ReceiptText,
    backLink: "/finance",
  };

  if (expenseQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Nao foi possivel carregar a despesa"
          error={expenseQuery.error}
        />
      </PageLayout>
    );
  }

  if (expenseQuery.isPending || !expenseQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados da despesa" />
      </PageLayout>
    );
  }

  const expense = expenseQuery.data;
  const isPaid = !!expense.paymentDate;
  const categoryLabel = expense.category
    ? EXPENSE_CATEGORY_LABEL[expense.category] ?? expense.category
    : "Sem categoria";

  const handleDelete = () => {
    if (!expense.id) return;

    deleteExpense.mutate(
      { id: expense.id },
      {
        onSuccess: () => navigate("/finance"),
      },
    );
  };

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_300ms_ease-out_both]">
        <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
          <div>
            <div className="flex flex-wrap items-center gap-2">
              <span className="badge badge-secondary badge-outline px-3 py-3">
                {categoryLabel}
              </span>
            </div>
            <h2 className="mt-3 text-2xl font-bold text-base-content">
              {expense.description ?? "Despesa sem descricao"}
            </h2>
            <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
              Registro operacional lançado em despesas gerais.
            </p>
          </div>

          <div className="flex flex-col gap-2 sm:flex-row">
            {expense.id ? (
              <ToggleExpensePaymentButton
                isPaid={isPaid}
                expenseId={expense.id}
              />
            ) : null}
            <Button type="button" size="sm" onClick={() => setIsEditOpen(true)}>
              <Edit className="mr-1 h-4 w-4" />
              Editar
            </Button>
            <Button
              type="button"
              size="sm"
              variant="danger"
              onClick={() => setIsDeleteOpen(true)}
            >
              <Trash2 className="mr-1 h-4 w-4" />
              Excluir
            </Button>
          </div>
        </div>

        <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
          <DetailItem
            label="Valor"
            value={brl.format(expense.amount ?? 0)}
            className="font-mono text-error"
          />
          <DetailItem
            label="Data"
            value={expense.date ? formatDateShortYear(expense.date) : "-"}
          />
          <DetailItem
            label="Pagamento"
            value={
              expense.paymentDate
                ? formatDateShortYear(expense.paymentDate)
                : "Pendente"
            }
            className={isPaid ? "text-success" : "text-warning"}
          />
          <DetailItem label="Categoria" value={categoryLabel} />
          <DetailItem label="Identificador" value={expense.id ?? "-"} />
        </div>
      </section>

      {isEditOpen ? (
        <div className="modal modal-open">
          <div className="modal-box max-w-lg border border-base-300 bg-base-100 shadow-2xl">
            <h3 className="mb-1 text-lg font-bold">Editar Despesa</h3>
            <p className="mb-4 text-sm text-base-content/60">
              Atualize os dados da despesa selecionada.
            </p>
            <ExpenseForm
              initialData={expense}
              onSuccess={() => setIsEditOpen(false)}
              onCancel={() => setIsEditOpen(false)}
            />
          </div>
        </div>
      ) : null}

      <DeleteConfirmationModal
        isOpen={isDeleteOpen}
        onClose={() => setIsDeleteOpen(false)}
        onConfirm={handleDelete}
        title="Excluir despesa"
        isItemPending={deleteExpense.isPending}
        itemName="despesa"
      />
    </PageLayout>
  );
}
