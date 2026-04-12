import { ErrorCard } from "@/components/ui/error-card";
import { PageLoading } from "@/components/ui/page-loading";
import styles from "@/features/dashboard/DashboardPage.module.css";
import { useGetDashboardSummary } from "@/kubb";
import { getFriendlyErrorMessage } from "@/lib/shared/api-errors";
import { brl } from "@/lib/utils/formatter";
import { PizzaChart } from "./components/PizzaChart";

function getCurrentYearMonth() {
  const now = new Date();

  return {
    year: now.getFullYear(),
    month: now.getMonth() + 1,
  };
}

export function DashboardPage() {
  const { year, month } = getCurrentYearMonth();
  const dashboardQuery = useGetDashboardSummary({
    year,
    month,
  });

  const kpiItems = [
    {
      label: "Alunos ativos",
      value: dashboardQuery.data?.activeStudentsInMonth ?? 0,
    },
    {
      label: "Aulas no mês",
      value: dashboardQuery.data?.classesInMonth ?? 0,
    },
    {
      label: "Receita no mês",
      value: brl.format(dashboardQuery.data?.revenueInMonth ?? 0),
    },
    {
      label: "Custo no mês",
      value: brl.format(dashboardQuery.data?.costInMonth ?? 0),
    },
  ];

  if (dashboardQuery.isPending) {
    return <PageLoading message="Carregando painel..." />;
  }

  if (dashboardQuery.isError || !dashboardQuery.data) {
    return (
      <div className={styles.errorWrap}>
        <h1 className="app-text text-3xl font-bold">Painel</h1>
        <ErrorCard
          title="Ops, não foi possível carregar"
          description={getFriendlyErrorMessage(dashboardQuery.error)}
          onAction={dashboardQuery.refetch}
        />
      </div>
    );
  }

  return (
    <div className={styles.page}>
      <h1 className="app-text text-3xl font-bold">Painel</h1>
      <div className={styles.kpiGrid}>
        {kpiItems.map((item) => (
          <div
            key={item.label}
            className="card border border-base-300 bg-base-100 shadow-sm"
          >
            <div className="card-body gap-2">
              <h2 className="app-kpi-label">{item.label}</h2>
              <div className="app-kpi-value">{item.value}</div>
            </div>
          </div>
        ))}
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
            <PizzaChart data={dashboardQuery.data.charts ?? []} />
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm flex items-center justify-center min-h-[300px]">
          <div className="text-center p-8">
            <h3 className="text-base-content/40 font-medium italic">
              Gráfico de Evolução
            </h3>
            <p className="text-xs text-base-content/30 mt-1">
              Em desenvolvimento
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
