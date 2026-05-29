import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import { Button } from "@/components/ui/button";
import {useGetDespesas,} from "@/kubb";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";
import { Landmark, Plus } from "lucide-react";
import { useState } from "react";
import { DespesaForm } from "../components/DespesaForm";
import { DespesasTable } from "../components/DespesasTable";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { FinanceiroKpis } from "../components/FinanceiroKpis";

export function FinanceiroPage() {
  const [isDespesaFormOpen, setIsDespesaFormOpen] = useState(false);
  const { startDate, endDate, ...dateFilter } = usePageDateFilter();

  const despesasQuery = useGetDespesas({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    size: 9999,
    sort: ["date,desc"],
  });

  const totalGeneralExpenses = despesasQuery.data?.totalExpenses ?? 0;
  const pendingGeneralExpenses = despesasQuery.data?.pendingExpenses ?? 0;

  const handleOpenDespesaForm = () => {
    setIsDespesaFormOpen(true);
  };

  const handleCloseDespesaForm = () => {
    setIsDespesaFormOpen(false);
  };

  const headerProps = {
    description:
      "Acompanhe o panorama financeiro da instituicao com foco em fluxo, pendencias e movimentacoes.",
    title: "Financeiro",
    Icon: Landmark,
    backLink: "/",
    iconBg: "warning",
  } as const;

  if (despesasQuery.isError) {
    return <ErrorCard title="Não foi possível carregar a listagem de Responsáveis" error={despesasQuery.error}/>;
  }

  if (despesasQuery.isLoading) {
    return <LoadingSpinner text="Carregando Responsáveis..." />;
  }

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <FinanceiroKpis
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
            onClick={() => handleOpenDespesaForm()}
          >
            <Plus className="mr-2 h-4 w-4" />
            Nova Despesa
          </Button>
        </div>

        <DespesasTable
          despesas={despesasQuery.data?.expenses}
          isPending={despesasQuery.isPending}
          error={despesasQuery.error}
        />
        </section>

    {isDespesaFormOpen ? (
      <div className="modal modal-open">
        <div className="modal-box max-w-lg border border-base-300 bg-base-100 shadow-2xl">
          <h3 className="mb-1 text-lg font-bold">Nova Despesa</h3>
              <p className="mb-4 text-sm text-base-content/60">
                Registre uma nova despesa operacional.
              </p>
              <DespesaForm
                onSuccess={handleCloseDespesaForm}
                onCancel={handleCloseDespesaForm}
              />
            </div>
          </div>
      ) : null}
      </div>

    <PageDateFilterWidget startDate={startDate} endDate={endDate} {...dateFilter} />
    </PageLayout>
  );
}
