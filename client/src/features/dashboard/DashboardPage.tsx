import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import type { StudentResponse, EmployeeResponse, EventResponse } from "@/lib/schemas"
import { studentsApi, employeesApi, eventsApi, getFriendlyErrorMessage, type PageResponse } from "@/services/api"
import { Button } from "@/components/ui/button"
import { useState, useEffect } from "react"
import styles from "@/features/dashboard/DashboardPage.module.css"

export function DashboardPage() {

  const [studentsCount, setStudentsCount] = useState(0)
  const [employeesCount, setEmployeesCount] = useState(0)
  const [eventsCount, setEventsCount] = useState(0)
  const [revenue, setRevenue] = useState(0)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setError(null)
        setLoading(true)
        const [studentsRes, employeesRes, eventsRes] = await Promise.all([
          studentsApi.list(0, 20, "name", true),
          employeesApi.listActive(),
          eventsApi.list(),
        ])

        const studentsPage: PageResponse<StudentResponse> = studentsRes.data
        const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data
        const eventsPage: PageResponse<EventResponse> = eventsRes.data

        setStudentsCount(studentsPage.totalElements)
        setEmployeesCount(employeesPage.totalElements)
        setEventsCount(eventsPage.totalElements)
        //TODO Move this logic to the backend
        // Calculate revenue from events
        const total = eventsPage.content.reduce(
          (sum, event) => sum + Number(event.payment),
          0
        )

        setRevenue(total)
      } catch (error) {
        console.error("Falha ao carregar o painel:", error)
        setError(getFriendlyErrorMessage(error))
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  if (loading) return <div>Carregando...</div>

  if (error) {
    return (
      <div className={styles.errorWrap}>
        <h1 className="text-3xl font-bold text-gray-900">Painel</h1>
        <Card>
          <CardHeader>
            <CardTitle>Ops, nao foi possivel carregar</CardTitle>
          </CardHeader>
          <CardContent className="space-y-3">
            <p className="text-sm text-muted-foreground">{error}</p>
            <Button type="button" onClick={() => window.location.reload()}>
              Tentar novamente
            </Button>
          </CardContent>
        </Card>
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
