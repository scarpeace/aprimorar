import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { PageErrorState } from "@/components/ui/page-error-state"
import { PageLoadingState } from "@/components/ui/page-loading-state"
import type { StudentResponse, EmployeeResponse, EventResponse } from "@/lib/schemas"
import { studentsApi, employeesApi, eventsApi, getFriendlyErrorMessage, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useMemo, useState } from "react"
import styles from "@/features/dashboard/DashboardPage.module.css"

export function DashboardPage() {
  const [studentsCount, setStudentsCount] = useState(0)
  const [employeesCount, setEmployeesCount] = useState(0)
  const [eventsCount, setEventsCount] = useState(0)
  const [totalPayments, setTotalPayments] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const currencyFormatter = useMemo(
    () =>
      new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      }),
    []
  )

  const fetchDashboardData = useCallback(async () => {
    try {
      setError(null)
      setLoading(true)

      const [studentsRes, employeesRes] = await Promise.all([
        studentsApi.list(0, 1, "name"),
        employeesApi.listActive(0, 1, "name"),
      ])

      const studentsPage: PageResponse<StudentResponse> = studentsRes.data
      const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data

      let currentPage = 0
      let lastPage = false
      let totalEventPayments = 0
      let totalEventCount = 0

      while (!lastPage) {
        const eventsRes = await eventsApi.list(currentPage, 100)
        const eventsPage: PageResponse<EventResponse> = eventsRes.data

        totalEventCount = eventsPage.totalElements
        totalEventPayments += eventsPage.content.reduce((sum, event) => sum + Number(event.payment), 0)
        lastPage = eventsPage.last
        currentPage += 1
      }

      setStudentsCount(studentsPage.totalElements)
      setEmployeesCount(employeesPage.totalElements)
      setEventsCount(totalEventCount)
      setTotalPayments(totalEventPayments)
    } catch (error) {
      console.error("Falha ao carregar o painel:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchDashboardData()
  }, [fetchDashboardData])

  if (loading) return <PageLoadingState label="Carregando painel..." />

  if (error) {
    return (
      <PageErrorState
        title="Painel"
        description="Acompanhe o panorama geral do sistema."
        errorMessage={error}
        onRetry={fetchDashboardData}
      />
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
            <div className="text-2xl font-bold">{currencyFormatter.format(totalPayments)}</div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
