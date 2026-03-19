import { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import styles from "@/features/dashboard/DashboardPage.module.css";
import { useDashboardSummary } from "@/features/dashboard/query/useDashboardSummary";
import { brl } from "@/lib/utils/formatter";
import { getFriendlyErrorMessage } from "@/lib/shared/api";

function getCurrentYearMonth() {
  const now = new Date();
  return {
    year: now.getFullYear(),
    month: now.getMonth() + 1,
  };
}

export function DashboardPage() {
  const { year, month } = getCurrentYearMonth();

  const { data, isLoading, isError, error, refetch } = useDashboardSummary({
    year,
    month,
  });

  if (isLoading) {
    return <PageLoading message="Carregando painel..." />;
  }

  if (isError) {
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

  if (!data) {
    return null;
  }

  const summary = data.kpis;

  return (
    <div className={styles.page}>
      <h1 className="app-text text-3xl font-bold">Painel</h1>

      <div className={styles.kpiGrid}>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Alunos ativos</h2>
            <div className="app-kpi-value">{summary.activeStudentsInMonth}</div>
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Aulas no mês</h2>
            <div className="app-kpi-value">{summary.classesInMonth}</div>
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Receita no mês</h2>
            <div className="app-kpi-value">
              {brl.format(summary.revenueInMonth)}
            </div>
          </div>
        </div>

        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Custo no mês</h2>
            <div className="app-kpi-value">
              {brl.format(summary.costInMonth)}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
