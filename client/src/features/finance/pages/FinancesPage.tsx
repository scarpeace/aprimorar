import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import {
  useGetAppointmentFinanceReport,
  useGetEmployeesAppointmentsFinanceReport,
  useGetExpenses,
  useGetStudentsAppointmentsFinanceReport,
} from "@/kubb";
import { useDateFilter } from "@/hooks/use-date-filter";
import { ArrowUpRight, Clock3, Landmark, Plus } from "lucide-react";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { EmployeesFinanceTable } from "../components/EmployeesFinanceTable";
import { ExpenseForm } from "../components/ExpenseForm";
import { ExpensesTable } from "../components/ExpensesTable";
import { FinanceKpiCard } from "../components/FinanceKpiCard";
import { FinanceSummarySection } from "../components/FinanceSummarySection";
import { StudentsFinanceTable } from "../components/StudentsFinanceTable";
import { brl } from "@/lib/utils/formatter";

export function FinancesPage() {
  const navigate = useNavigate();
  const [isExpenseFormOpen, setIsExpenseFormOpen] = useState(false);

  const { startDate, endDate } = useDateFilter();

  const summaryQuery = useGetAppointmentFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });
  const studentsFinanceQuery = useGetStudentsAppointmentsFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });
  const employeesFinanceQuery = useGetEmployeesAppointmentsFinanceReport({
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
        <div className="relative z-10 flex flex-col gap-3 rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_200ms_ease-out_both] sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-xl font-bold text-base-content">
              Resumo Institucional
            </h2>
            <p className="mt-1 text-sm text-base-content/60">
              Visualize o consolidado financeiro filtrado por periodo.
            </p>
          </div>
        </div>

        <FinanceSummarySection
          currentBalance={currentBalance}
          totalStudentCharged={totalStudentCharged}
          totalStudentPending={totalStudentPending}
          totalEmployeePaid={totalEmployeePaid}
          totalEmployeePending={totalEmployeePending}
          isError={summaryQuery.isError}
          error={summaryQuery.error}
        />

        <div className="grid gap-4 xl:grid-cols-2">
          <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
            <div className="mb-4">
              <div className="flex flex-wrap items-center gap-2">
                <span className="badge badge-primary badge-outline px-3 py-3">
                  Alunos
                </span>
              </div>
              <h2 className="mt-3 text-2xl font-bold text-base-content">
                Financeiro por aluno
              </h2>
              <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
                Valores recebidos e pendentes por aluno no periodo selecionado.
              </p>
            </div>

            <StudentsFinanceTable
              students={studentsFinanceQuery.data?.students}
              isPending={studentsFinanceQuery.isPending}
              error={studentsFinanceQuery.error}
            />
          </section>

          <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
            <div className="mb-4">
              <div className="flex flex-wrap items-center gap-2">
                <span className="badge badge-primary badge-outline px-3 py-3">
                  Colaboradores
                </span>
              </div>
              <h2 className="mt-3 text-2xl font-bold text-base-content">
                Financeiro por colaborador
              </h2>
              <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
                Valores pagos e pendentes por colaborador no periodo
                selecionado.
              </p>
            </div>

            <EmployeesFinanceTable
              employees={employeesFinanceQuery.data?.employees}
              isPending={employeesFinanceQuery.isPending}
              error={employeesFinanceQuery.error}
            />
          </section>
        </div>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
          <div className="flex justify-between mb-4">
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

            <div className="flex items-start gap-6">
              <FinanceKpiCard
                title="Pagas"
                value={brl.format(totalGeneralExpenses)}
                tone="secondary"
                icon={<ArrowUpRight className="h-5 w-5" />}
              />
              <FinanceKpiCard
                title="Pendentes"
                value={brl.format(pendingGeneralExpenses)}
                tone="warning"
                icon={<Clock3 className="h-5 w-5" />}
              />
              <Button
                type="button"
                variant="success"
                onClick={() => handleOpenExpenseForm()}
                className="sm:mb-1"
              >
                <Plus className="mr-2 h-4 w-4" />
                Nova Despesa
              </Button>
            </div>
          </div>

          <ExpensesTable
            expenses={expensesQuery.data?.expenses}
            isPending={expensesQuery.isPending}
            error={expensesQuery.error}
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
