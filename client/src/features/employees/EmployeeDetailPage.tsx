import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { UserCog } from "lucide-react"
import { eventContentLabels, type EventResponse } from "@/lib/schemas/event"
import type { EmployeeResponse } from "@/lib/schemas/employee"
import { employeesApi, eventsApi, getFriendlyErrorMessage, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import styles from "@/features/employees/EmployeeDetailPage.module.css"

function SummaryField({ label, value }: { label: string; value: string }) {
  return (
    <div className={styles.summaryItem}>
      <p className={styles.summaryLabel}>{label}</p>
      <p className={styles.summaryValue}>{value}</p>
    </div>
  )
}

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
        eventsApi.listByEmployee(id, 0, 100, "startDateTime"),
      ])

      const eventsPage: PageResponse<EventResponse> = eventsRes.data

      setEmployee(employeeRes.data)
      setLinkedEvents(eventsPage.content)
      setEmployeeEventsCount(eventsPage.totalElements)
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
            <SummaryField label="Nome completo" value={employee.name} />
            <SummaryField label="E-mail" value={employee.email} />
            <SummaryField label="Cargo" value={employee.role} />
            <SummaryField label="Contato" value={employee.contact} />
            <SummaryField label="CPF" value={employee.cpf} />
            <SummaryField label="Chave PIX" value={employee.pix} />
            <SummaryField label="Data de nascimento" value={employee.birthdate} />
            <SummaryField label="Status" value={employee.active ? "Ativo" : "Inativo"} />
            <SummaryField label="Criado em" value={employee.createdAt} />
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
            <p className="text-sm text-muted-foreground">Este colaborador ainda não possui eventos vinculados.</p>
          ) : (
            <div className={styles.tableWrap}>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Título</TableHead>
                    <TableHead>Aluno</TableHead>
                    <TableHead>Conteúdo</TableHead>
                    <TableHead>Início</TableHead>
                    <TableHead>Fim</TableHead>
                    <TableHead>Preço</TableHead>
                    <TableHead>Ações</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {linkedEvents.map((event) => (
                    <TableRow key={event.id}>
                      <TableCell>{event.title}</TableCell>
                      <TableCell>{event.studentName}</TableCell>
                      <TableCell>{eventContentLabels[event.content]}</TableCell>
                      <TableCell>{event.startDateTime}</TableCell>
                      <TableCell>{event.endDateTime}</TableCell>
                      <TableCell>{event.price}</TableCell>
                      <TableCell>
                        <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/events/${event.id}`}>
                          Ver evento
                        </Link>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
