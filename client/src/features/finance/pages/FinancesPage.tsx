import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import { Button } from "@/components/ui/button";
import {useGetDespesas,} from "@/kubb";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";
import { Landmark, Plus } from "lucide-react";
import { useState } from "react";
import { ExpenseForm } from "../components/ExpenseForm";
import { ExpensesTable } from "../components/ExpensesTable";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { FinancialKpis } from "../components/FinancialKpis";

export function FinancesPage() {
  const [isExpenseFormOpen, setIsExpenseFormOpen] = useState(false);
  const { startDate, endDate, ...dateFilter } = usePageDateFilter();

  const expensesQuery = useGetDespesas({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    size: 9999,
    sort: ["date,desc"],
  });

  const totalGeneralExpenses = expensesQuery.data?.totalExpenses ?? 0;
  const pendingGeneralExpenses = expensesQuery.data?.pendingExpenses ?? 0;

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

  if (expensesQuery.isError) {
    return <ErrorCard title="Não foi possível carregar a listagem de Responsáveis" error={expensesQuery.error}/>;
  }

  if (expensesQuery.isLoading) {
    return <LoadingSpinner text="Carregando Responsáveis..." />;
  }

  // if (!expensesQuery.data || !expensesQuery.data.content || expensesQuery.data.totalElements === 0) {
  //   return (
  //     <EmptyCard
  //       title="Nenhum responsável encontrado"
  //       description="Ajuste a busca ou o filtro de arquivados para localizar os cadastros desejados."
  //       action={<Button variant="outline" onClick={handleCleanFilter}>Limpar filtros<BrushCleaning size={18} /></Button>}
  //     />
  //   );
  // }

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <FinancialKpis
          totalDespesas={totalGeneralExpenses}
          totalDespesasPendentes={pendingGeneralExpenses}
        />

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
    <PageDateFilterWidget startDate={startDate} endDate={endDate} {...dateFilter} />
      </div>
    </PageLayout>
  );
}
