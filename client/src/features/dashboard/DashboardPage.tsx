import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import { studentsApi, employeesApi, eventsApi, getFriendlyErrorMessage } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import styles from "@/features/dashboard/DashboardPage.module.css"

export function DashboardPage() {

  const [studentsCount, setStudentsCount] = useState(0)
  const [employeesCount, setEmployeesCount] = useState(0)
  const [eventsCount, setEventsCount] = useState(0)
  const [revenue, setRevenue] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

 const loadDashboard = useCallback(async () => {
  try {
    setError(null)
    setLoading(true)
    const [studentsResult, employeesResult, eventsResult] = await Promise.allSettled([
      studentsApi.list(0, 20, "name"),
      employeesApi.list(),
      eventsApi.list(),
    ])
    console.log(studentsResult);

    let hasSuccess = false

    if (studentsResult.status === "fulfilled") {
      setStudentsCount(studentsResult.value.page.totalElements)
      hasSuccess = true
    } else {
      console.error("Falha ao carregar alunos:", studentsResult.reason)
      setStudentsCount(0)
    }

    if (employeesResult.status === "fulfilled") {
      setEmployeesCount(employeesResult.value.page.totalElements)
      hasSuccess = true
    } else {
      console.error("Falha ao carregar colaboradores:", employeesResult.reason)
      setEmployeesCount(0)
    }

    if (eventsResult.status === "fulfilled") {
      setEventsCount(eventsResult.value.page.totalElements)
      const total = eventsResult.value.content.reduce(
        (sum, event) => sum + Number(event.payment),
        0
      )
      setRevenue(total)
      hasSuccess = true

    } else {
      console.error("Falha ao carregar eventos:", eventsResult.reason)
      setEventsCount(0)
      setRevenue(0)
    }
    if (!hasSuccess) {
      setError("Não foi possível carregar os dados do painel.")
    }
  } catch (error) {
    console.error("Falha ao carregar o painel:", error)
    setError(getFriendlyErrorMessage(error))
  } finally {
    setLoading(false)
  }
}, [])

useEffect(() => {
  loadDashboard()
}, [loadDashboard])

  if (loading) return <LoadingState message="Carregando painel..." />

  if (error) {
    return (
      <div className={styles.errorWrap}>
        <h1 className="text-3xl font-bold text-gray-900">Painel</h1>
        <ErrorState
          title="Ops, não foi possível carregar"
          description={error}
          actionLabel="Tentar novamente"
          onAction={loadDashboard}
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <h1 className="text-3xl font-bold text-gray-900">Painel</h1>
      <div className={styles.kpiGrid}>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Alunos ativos</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{studentsCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Colaboradores ativos</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{employeesCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Eventos</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{eventsCount}</div>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Custo (pagamentos)</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">R$ {revenue}</div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
