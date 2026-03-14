import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import { SummaryField } from "@/components/ui/summary-field"
import { UserCog } from "lucide-react"
import type { EventResponse } from "@/lib/schemas/event"
import type { EmployeeResponse } from "@/lib/schemas/employee"
import { dutyLabels } from "@/features/employees/dutyLabels"
import { employeesApi, eventsApi, getFriendlyErrorMessage } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import styles from "@/features/employees/EmployeeDetailPage.module.css"
import type { PageResponse } from "@/lib/schemas/page-response"
import { EventsTable } from "@/components/ui/events-table"

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [employee, setEmployee] = useState<EmployeeResponse | null>(null)
  const [linkedEvents, setLinkedEvents] = useState<EventResponse[]>([])
  const [employeeEventsCount, setEmployeeEventsCount] = useState(0)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadEmployeeData = useCallback(async () => {
    if (!id) {
      setError("ID do colaborador não informado.")
      return
    }

    try {
      setLoading(true)
      setError(null)

      const [employeeRes, eventsRes] = await Promise.all([
        employeesApi.getById(id),
        eventsApi.listByEmployee(id, 0, 100, "startDate"),
      ])

      const eventsPage: PageResponse<EventResponse> = eventsRes;

      setEmployee(employeeRes)
      setLinkedEvents(eventsPage.content)
      setEmployeeEventsCount(eventsPage.page.totalElements)
    } catch (loadError) {
      console.error("Falha ao carregar colaborador:", loadError)
      setError(getFriendlyErrorMessage(loadError))
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    loadEmployeeData()
  }, [loadEmployeeData])

  if (loading) return <LoadingState message="Carregando colaborador..." />

  if (error) {
    return (
      <div className={styles.page}>
        <ErrorState
          title="Não foi possível carregar"
          description={error}
          actionLabel="Tentar novamente"
          onAction={loadEmployeeData}
        />
      </div>
    )
  }

  if (!employee) {
    return (
      <div className={styles.page}>
        <EmptyState
          title="Colaborador não encontrado"
          description="Não encontramos os dados deste colaborador."
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-green-100">
            <UserCog className="h-6 w-6 text-green-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Detalhes do colaborador</h1>
            <p className="text-sm text-gray-500">Veja e gerencie as informações do colaborador</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/employees">← Voltar para colaboradores</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Resumo do colaborador</CardTitle>
          <CardDescription>Dados completos de cadastro, contato e status.</CardDescription>
        </CardHeader>
        <CardContent>
          <div className={styles.summaryGrid}>
            <SummaryField title="Nome completo">{employee.name}</SummaryField>
            <SummaryField title="E-mail">{employee.email}</SummaryField>
            <SummaryField title="Cargo">{dutyLabels[employee.duty]}</SummaryField>
            <SummaryField title="Contato">{employee.contact}</SummaryField>
            <SummaryField title="CPF">{employee.cpf}</SummaryField>
            <SummaryField title="Chave PIX">{employee.pix}</SummaryField>
            <SummaryField title="Data de nascimento">{employee.birthdate}</SummaryField>
            <SummaryField title="Status">{employee.archivedAt ? "Arquivado" : "Ativo"}</SummaryField>
            <SummaryField title="Criado em">{employee.createdAt}</SummaryField>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Eventos vinculados</CardTitle>
          <CardDescription>Total de eventos vinculados a este colaborador: {employeeEventsCount}</CardDescription>
        </CardHeader>
        <CardContent>
          {linkedEvents.length === 0 ? (
            <p className="text-sm text-muted-foreground">Este aluno ainda não possui eventos vinculados.</p>
          ) : (
            <div className={styles.tableWrap}>
              <EventsTable variant="employeePage" events={linkedEvents} />
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
