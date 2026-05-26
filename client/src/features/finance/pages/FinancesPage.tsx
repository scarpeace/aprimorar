import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import {useGetDespesas,} from "@/kubb";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";
import { BanknoteArrowDown, BanknoteArrowUp, GraduationCap, HandCoins, Landmark, Plus, UserCog } from "lucide-react";
import { useState } from "react";
import { ExpenseForm } from "../components/ExpenseForm";
import { ExpensesTable } from "../components/ExpensesTable";
import { FinanceSummarySection } from "../components/FinanceSummarySection";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";

export function FinancesPage() {
  const [isExpenseFormOpen, setIsExpenseFormOpen] = useState(false);

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  // const summaryQuery = useGetDashboardSummary({
  //   startDate: startDate?.toISOString(),
  //   endDate: endDate?.toISOString(),
  // });
  const expensesQuery = useGetDespesas({
    startDate: startDate?.toISOString().slice(0, 10),
    endDate: endDate?.toISOString().slice(0, 10),
    size: 9999,
    sort: ["date,desc"],
  });
  // const studentsWithFinanceQuery = useGetStudentsWithFinance({
  //   size: 1,
  //   startDate: startDate?.toISOString(),
  //   endDate: endDate?.toISOString(),
  // });
  // const employeesWithFinanceQuery = useGetEmployeesWithFinance({
  //   size: 1,
  //   startDate: startDate?.toISOString(),
  //   endDate: endDate?.toISOString(),
  // });

  const totalGeneralExpenses = expensesQuery.data?.totalExpenses ?? 0;
  const pendingGeneralExpenses = expensesQuery.data?.pendingExpenses ?? 0;
  // const currentBalance = summaryQuery.data?.balance ?? 0;
  // const studentFinanceSummary = studentsWithFinanceQuery.data?.financeSummary;
  // const employeeFinanceSummary = employeesWithFinanceQuery.data?.financeSummary;

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

  // if (summaryQuery.isPending) {
  //   return (
  //     <PageLayout {...headerProps}>
  //       <LoadingCard title="Carregando panorama financeiro" />
  //     </PageLayout>
  //   );
  // }

  // if (summaryQuery.isError) {
  //   return (
  //     <PageLayout {...headerProps}>
  //       <ErrorCard
  //         title="Nao foi possivel carregar o financeiro"
  //         error={summaryQuery.error}
  //       />
  //     </PageLayout>
  //   );
  // }

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">

        {/*<FinanceSummarySection
          currentBalance={currentBalance}
        />*/}

        <section className="flex flex-row justify-between rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
          {/*<div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
            <h1 className="text-2xl flex gap-3 items-center font-bold text-base-content"><GraduationCap size={30}/> Alunos</h1>

            <KpiCard
              label="Total pago"
              value={<span className="text-success">{brl.format(studentFinanceSummary?.totalCharged ?? 0)}</span>}
              Icon={BanknoteArrowUp}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
            />

            <KpiCard
              label="Total pendente"
              value={<span className="text-warning">{brl.format(studentFinanceSummary?.totalPending ?? 0)}</span>}
              Icon={BanknoteArrowDown}
              className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
              />
        </div>*/}

          {/*<div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
            <h1 className="text-2xl flex gap-3 items-center font-bold text-base-content"><UserCog size={30}/>Colaboradores</h1>

            <KpiCard
              label="Total pago"
              value={<span className="text-success">{brl.format(employeeFinanceSummary?.totalPaid ?? 0)}</span>}
              Icon={BanknoteArrowUp}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
            />

            <KpiCard
              label="Total pendente"
              value={<span className="text-warning">{brl.format(employeeFinanceSummary?.totalPending ?? 0)}</span>}
              Icon={BanknoteArrowDown}
              className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
            />
          </div>*/}

          <div className={`flex flex-col rounded-xl p-3 gap-4 border-2 border-base-300`}>
            <h1 className="text-2xl flex gap-3 items-center font-bold text-base-content"><HandCoins size={30}/>Despesas Gerais</h1>

            <KpiCard
              label="Despesas Gerais Pagas"
              value={<span className="text-success">{brl.format(totalGeneralExpenses)}</span>}
              Icon={BanknoteArrowUp}
              className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
            />
            <KpiCard
              label="Despesas Gerais Pendentes"
              value={<span className="text-warning">{brl.format(pendingGeneralExpenses)}</span>}
              Icon={BanknoteArrowDown}
              className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
            />
          </div>
      </section>

      <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
        <div className="flex justify-between items-start mb-4">
          <div className="flex flex-col">
            <h2 className="mt-3 text-2xl font-bold text-base-content">Despesas gerais</h2>

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
    <PageDateFilterWidget {...dateFilter} />
      </div>
    </PageLayout>
  );
}
