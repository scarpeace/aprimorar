import { useQuery } from "@tanstack/react-query"
import { ErrorCard } from "@/components/ui/error-card"
import { PageLoading } from "@/components/ui/page-loading"
import styles from "@/features/dashboard/DashboardPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { brl } from "@/lib/shared/formatter"
import { employeesApi, eventsApi, getFriendlyErrorMessage, studentsApi } from "@/services/api"

type DashboardSummary = {
  studentsCount: number
  employeesCount: number
  eventsCount: number
  revenue: number
}

export function DashboardPage() {
  const {
    data,
    isLoading,
    isError,
    error,
    refetch,
  } = useQuery({
    queryKey: queryKeys.dashboard,
    queryFn: async (): Promise<DashboardSummary> => {
      const [studentsResult, employeesResult, eventsResult] = await Promise.allSettled([
        studentsApi.list(),
        employeesApi.list(),
        eventsApi.list(),
      ])

      let hasSuccess = false

      const studentsCount = studentsResult.status === "fulfilled" ? studentsResult.value.page.totalElements : 0

      if (studentsResult.status === "fulfilled") {
        hasSuccess = true
      } else {
        console.error("Falha ao carregar alunos:", studentsResult.reason)
      }

      const employeesCount = employeesResult.status === "fulfilled" ? employeesResult.value.page.totalElements : 0

      if (employeesResult.status === "fulfilled") {
        hasSuccess = true
      } else {
        console.error("Falha ao carregar colaboradores:", employeesResult.reason)
      }

      const eventsCount = eventsResult.status === "fulfilled" ? eventsResult.value.page.totalElements : 0
      const revenue =
        eventsResult.status === "fulfilled"
          ? eventsResult.value.content.reduce((sum, event) => sum + Number(event.payment), 0)
          : 0

      if (eventsResult.status === "fulfilled") {
        hasSuccess = true
      } else {
        console.error("Falha ao carregar eventos:", eventsResult.reason)
      }

      if (!hasSuccess) {
        throw new Error("Não foi possível carregar os dados do painel.")
      }

      return {
        studentsCount,
        employeesCount,
        eventsCount,
        revenue,
      }
    },
  })

  if (isLoading) {
    return <PageLoading message="Carregando painel..." />
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
    )
  }

  const summary = data

  if (!summary) {
    return null
  }

  return (
    <div className={styles.page}>
      <h1 className="app-text text-3xl font-bold">Painel</h1>
      <div className={styles.kpiGrid}>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Alunos ativos</h2>
            <div className="app-kpi-value">{summary.studentsCount}</div>
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Colaboradores ativos</h2>
            <div className="app-kpi-value">{summary.employeesCount}</div>
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Eventos</h2>
            <div className="app-kpi-value">{summary.eventsCount}</div>
          </div>
        </div>
        <div className="card border border-base-300 bg-base-100 shadow-sm">
          <div className="card-body gap-2">
            <h2 className="app-kpi-label">Custo (pagamentos)</h2>
            <div className="app-kpi-value">{brl.format(summary.revenue)}</div>
          </div>
        </div>
      </div>
    </div>
  )
}
