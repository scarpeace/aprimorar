import { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import styles from "@/features/dashboard/DashboardPage.module.css";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { brl } from "@/lib/utils/formatter";
import { PizzaChart } from "./components/PizzaChart";
import { useGetDashboardSummary } from "@/kubb";
function getCurrentYearMonth() {
  const now = new Date();
  return {
    year: now.getFullYear() + 1,
    month: now.getMonth() + 1,
  };
}
export function DashboardPage() {
  const { year, month } = getCurrentYearMonth();
  const { data, isLoading, isError, error, refetch } = useGetDashboardSummary({
    year,
    month,
  });


  if (isLoading) {
    return <PageLoading message="Carregando painel..." />;
  }
  if (isError || !data) {
    return (
      <div className={styles.errorWrap}>
        <h1 className="app-text text-3xl font-bold">Painel</h1>
        <ErrorCard
          title="Ops, não foi possível carregar"
          description={getFriendlyErrorMessage(error)}
          onAction={refetch}
        />
      </div>
    );
  }
  return (
    <div className={styles.page}>
      <h1 className="app-text text-3xl font-bold">Painel</h1>
      <div className={styles.kpiGrid}>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Alunos ativos</h2>
            <div className="app-kpi-value">{data.activeStudentsInMonth ?? 0}</div>
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Aulas no mês</h2>
            <div className="app-kpi-value">{data.classesInMonth ?? 0}</div>
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpitext-amber-100-label">Receita no mês</h2>
            <div className="app-kpi-value">
              {brl.format(data.revenueInMonth ?? 0)}
            </div>
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Custo no mês</h2>
            <div className="app-kpi-value">{brl.format(data.costInMonth ?? 0)}</div>
          </div>
        </div>
      </div>
      <div className="grid grid-cols-1 gap-6 lg:grid-cols-2">
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body">
            <h2 className="card-title text-lg font-semibold">
              Distribuição de Conteúdo
            </h2>
            <p className="text-sm text-base-content/60 mb-4">
              Visualização das aulas por categoria de atendimento.
            </p>
            <PizzaChart data={data.charts ?? []} />
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm flex items-center justify-center min-h-[300px]">
          <div className="text-center p-8">
            <h3 className="text-base-content/40 font-medium italic">
              Gráfico de Evolução
            </h3>
            <p className="text-xs text-base-content/30 mt-1">Em desenvolvimento</p>
          </div>
        </div>
      </div>
    </div>
  );
}
