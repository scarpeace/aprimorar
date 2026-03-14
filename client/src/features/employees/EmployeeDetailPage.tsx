import { useQuery } from "@tanstack/react-query"
import type { ReactNode } from "react"
import { UserCog } from "lucide-react"
import { Link, useParams } from "react-router-dom"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import { dutyLabels } from "@/features/employees/dutyLabels"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/employees/EmployeeDetailPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { employeesApi, eventsApi, getFriendlyErrorMessage } from "@/services/api"

const EMPLOYEE_EVENTS_PARAMS = { page: 0, size: 100, sortBy: "startDate" }

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>()
  const employeeId = id ?? ""

  const employeeQuery = useQuery({
    queryKey: queryKeys.employees.detail(employeeId),
    queryFn: () => employeesApi.getById(employeeId),
    enabled: Boolean(id),
  })

  const employeeEventsQuery = useQuery({
    queryKey: queryKeys.events.byEmployee(employeeId, EMPLOYEE_EVENTS_PARAMS),
    queryFn: () =>
      eventsApi.listByEmployee(
        employeeId,
        EMPLOYEE_EVENTS_PARAMS.page,
        EMPLOYEE_EVENTS_PARAMS.size,
        EMPLOYEE_EVENTS_PARAMS.sortBy
      ),
    enabled: Boolean(id),
  })

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do colaborador não informado." />
      </div>
    )
  }

  const refetchAll = async () => {
    await Promise.all([employeeQuery.refetch(), employeeEventsQuery.refetch()])
  }

  if (employeeQuery.isLoading || employeeEventsQuery.isLoading) {
    return <PageLoading message="Carregando colaborador..." />
  }

  if (employeeQuery.isError || employeeEventsQuery.isError) {
    const queryError = employeeQuery.error ?? employeeEventsQuery.error

    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(queryError)} onAction={refetchAll} />
      </div>
    )
  }

  const employee = employeeQuery.data

  if (!employee) {
    return (
      <div className={styles.page}>
        <EmptyCard title="Colaborador não encontrado" description="Não encontramos os dados deste colaborador." />
      </div>
    )
  }

  const linkedEvents = employeeEventsQuery.data?.content ?? []
  const employeeEventsCount = employeeEventsQuery.data?.page.totalElements ?? 0

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: employee.name },
    { label: "E-mail", value: employee.email },
    { label: "Cargo", value: dutyLabels[employee.duty] },
    { label: "Contato", value: employee.contact },
    { label: "CPF", value: employee.cpf },
    { label: "Chave PIX", value: employee.pix },
    { label: "Data de nascimento", value: employee.birthdate },
    { label: "Status", value: employee.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Criado em", value: employee.createdAt },
  ]

  return (
    <div className={styles.page}>
      <PageHeader
        action={
          <Link className="btn btn-outline" to="/employees">
            Voltar para colaboradores
          </Link>
        }
        description="Veja e gerencie as informações do colaborador"
        leading={
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-success/15">
            <UserCog className="h-6 w-6 text-success" />
          </div>
        }
        title="Detalhes do colaborador"
        titleClassName="text-2xl font-bold app-text"
      />

      <SectionCard title="Resumo do colaborador" description="Dados completos de cadastro, contato e status.">
        <div className={styles.summaryGrid}>
          {summaryItems.map((item) => (
            <SummaryItem key={item.label} label={item.label} value={item.value} />
          ))}
        </div>
      </SectionCard>

      <SectionCard
        title="Eventos vinculados"
        description={`Total de eventos vinculados a este colaborador: ${employeeEventsCount}`}
      >
        {linkedEvents.length === 0 ? (
          <p className="text-sm app-text-muted">Este colaborador ainda não possui eventos vinculados.</p>
        ) : (
          <div className="app-table-wrap">
            <EventsTable variant="employeePage" events={linkedEvents} />
          </div>
        )}
      </SectionCard>
    </div>
  )
}
