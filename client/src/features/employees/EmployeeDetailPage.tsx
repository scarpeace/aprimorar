import type { ReactNode } from "react"
import { UserCog } from "lucide-react"
import { useNavigate, useParams } from "react-router-dom"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import { dutyLabels } from "@/features/employees/dutyLabels"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/employees/EmployeeDetailPage.module.css"
import { getFriendlyErrorMessage } from "@/services/api"
import { useEmployeeDetailQuery, useEmployeeEventsQuery } from "./hooks/use-employees"
import { DeleteEmployeeButton } from "./components/DeleteEmployeeButton"
import { EditEmployeeButton } from "./components/EditEmployeeButton"
import { ArchiveEmployeeButton } from "./components/ArchiveEmployeeButton"

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>()
  const employeeId = id ?? ""
  const navigate = useNavigate()

  const { data: employeeData, error: employeeDataError, isLoading: isEmployeeLoading, isFetched: isEmployeeFetched } = useEmployeeDetailQuery(employeeId)
  const { data: employeeEvents, error: employeeEventsDataError, isLoading: isEmployeeEventsLoading, isFetched: isEmployeeEventsFetched } = useEmployeeEventsQuery(employeeId)

  const employeeEventsCount = employeeEvents?.page.totalElements ?? 0

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: employeeData?.name },
    { label: "E-mail", value: employeeData?.email },
    { label: "Cargo", value: dutyLabels[employeeData?.duty as keyof typeof dutyLabels] },
    { label: "Contato", value: employeeData?.contact },
    { label: "CPF", value: employeeData?.cpf },
    { label: "Chave PIX", value: employeeData?.pix },
    { label: "Data de nascimento", value: employeeData?.birthdate },
    { label: "Status", value: employeeData?.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Criado em", value: employeeData?.createdAt },
  ]

  return (
    <div className={styles.page}>
      <PageHeader
        link="/employees"
        icon={UserCog}
        description="Veja e gerencie as informações do colaborador"
        title="Detalhes do colaborador"
        titleClassName="text-2xl font-bold app-text"
      />

      {/* COLABORADOR */}
      <SectionCard
        title="Resumo do colaborador"
        description="Dados completos de cadastro, contato e status."
        headerAction={
          <>
            <DeleteEmployeeButton employeeId={employeeId} />
            <EditEmployeeButton employeeId={employeeId} />
            <ArchiveEmployeeButton employeeId={employeeId} isArchived={!!employeeData?.archivedAt} />
          </>
        }
      >

        {isEmployeeLoading && <PageLoading message="Carregando colaborador..." />}

        {employeeDataError && (
          <div className={styles.page}>
            <ErrorCard
              description={getFriendlyErrorMessage(employeeDataError)}
              actionLabel="Voltar para listagem de colaboradores"
              onAction={() => navigate("/employees")} />
          </div>
        )}

        {isEmployeeFetched && (
          <div className={styles.summaryGrid}>
            {summaryItems.map((item) => (
              <SummaryItem key={item.label} label={item.label} value={item.value} />
            ))}
          </div>
        )}
      </SectionCard>

      {/* EVENTOS DO COLABORADOR */}
      <SectionCard
        title="Eventos vinculados"
        description={`Total de eventos vinculados a este colaborador: ${employeeEventsCount}`}
      >
        {isEmployeeEventsLoading && <PageLoading message="Carregando eventos..." />}

        {employeeEventsDataError && (
          <div className={styles.page}>
            <ErrorCard
              description={getFriendlyErrorMessage(employeeEventsDataError)}
              actionLabel="Voltar para listagem de colaboradores"
              onAction={() => navigate("/employees")} />
          </div>
        )}

        {/* //TODO deve ter um jeito melhor de enviar a eventsPage */}
        {isEmployeeEventsFetched && (
          <EventsTable
            eventsPage={employeeEvents ?? { content: [], page: { totalElements: 0, totalPages: 0, number: 0, size: 0 } }}
            loading={isEmployeeEventsLoading}
            error={employeeEventsDataError ? getFriendlyErrorMessage(employeeEventsDataError) : ""}
            variant="employeePage"
          />
        )}
      </SectionCard>
    </div>
  )
}
