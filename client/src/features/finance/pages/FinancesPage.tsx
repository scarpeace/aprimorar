import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import {
  useGetAppointmentFinanceReport,
  useGetExpenses,
} from "@/kubb";
import { useDateFilter } from "@/hooks/use-date-filter";
import { Landmark, Plus } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { ExpenseForm } from "../components/ExpenseForm";
import { ExpensesTable } from "../components/ExpensesTable";
import { FinanceSummarySection } from "../components/FinanceSummarySection";
import { useExpenseMutations } from "../hooks/useExpenseMutations";

export function FinancesPage() {
  const navigate = useNavigate();
  const [isExpenseFormOpen, setIsExpenseFormOpen] = useState(false);
  const { toggleExpensePayment } = useExpenseMutations();

  const { startDate, endDate } = useDateFilter();

  const summaryQuery = useGetAppointmentFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });
  const expensesQuery = useGetExpenses({
    startDate: startDate?.toISOString().slice(0, 10),
    endDate: endDate?.toISOString().slice(0, 10),
    size: 9999,
    sort: ["date,desc"],
  });

  const totalStudentCharged = summaryQuery.data?.totalStudentCharged ?? 0;
  const totalStudentPending = summaryQuery.data?.totalStudentPending ?? 0;
  const totalEmployeePaid = summaryQuery.data?.totalEmployeePaid ?? 0;
  const totalEmployeePending = summaryQuery.data?.totalEmployeePending ?? 0;
  const totalGeneralExpenses = expensesQuery.data?.totalExpenses ?? 0;
  const pendingGeneralExpenses = expensesQuery.data?.pendingExpenses ?? 0;
  const currentBalance = summaryQuery.data?.balance ?? 0;

  const handleOpenExpenseForm = () => {
    setIsExpenseFormOpen(true);
  };

  const handleCloseExpenseForm = () => {
    setIsExpenseFormOpen(false);
  };

  const headerProps = {
    description:
      "Acompanhe o panorama financeiro da instituicao com foco em fluxo, pendencias e movimentacoes.",
    title: "Financeiro",
    Icon: Landmark,
    backLink: "/",
  };

  if (summaryQuery.isPending) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando panorama financeiro" />
      </PageLayout>
    );
  }

  if (summaryQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Nao foi possivel carregar o financeiro"
          error={summaryQuery.error}
        />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">

        <FinanceSummarySection
          currentBalance={currentBalance}
          totalStudentCharged={totalStudentCharged}
          totalStudentPending={totalStudentPending}
          totalEmployeePaid={totalEmployeePaid}
          totalEmployeePending={totalEmployeePending}
          totalGeneralExpenses={totalGeneralExpenses}
          pendingGeneralExpenses={pendingGeneralExpenses}
          isError={summaryQuery.isError}
          error={summaryQuery.error}
        />

        <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
          <div className="flex justify-between items-start mb-4">
            <div className="flex flex-col">
              <span className="badge badge-primary badge-outline px-3 py-3">
                Despesas Gerais
              </span>
              <h2 className="mt-3 text-2xl font-bold text-base-content">
                Despesas gerais
              </h2>

              <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
                Despesas operacionais registradas no periodo selecionado.
              </p>
            </div>

            <Button
              type="button"
              variant="success"
              onClick={() => handleOpenExpenseForm()}
            >
              <Plus className="mr-2 h-4 w-4" />
              Nova Despesa
            </Button>
          </div>

          <ExpensesTable
            expenses={expensesQuery.data?.expenses}
            isPending={expensesQuery.isPending}
            error={expensesQuery.error}
            toggleExpensePayment={toggleExpensePayment}
            onNavigate={(expense) => {
              if (expense.id) {
                navigate(`/finance/expenses/${expense.id}`);
              }
            }}
          />
        </section>
        {isExpenseFormOpen ? (
          <div className="modal modal-open">
            <div className="modal-box max-w-lg border border-base-300 bg-base-100 shadow-2xl">
              <h3 className="mb-1 text-lg font-bold">Nova Despesa</h3>
              <p className="mb-4 text-sm text-base-content/60">
                Registre uma nova despesa operacional.
              </p>
              <ExpenseForm
                onSuccess={handleCloseExpenseForm}
                onCancel={handleCloseExpenseForm}
              />
            </div>
          </div>
        ) : null}
        </div>


    </PageLayout>
  );
}
