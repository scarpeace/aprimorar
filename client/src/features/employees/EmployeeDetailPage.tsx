import type { ReactNode } from "react"
import { UserCog } from "lucide-react"
import { useNavigate, useParams } from "react-router-dom"
import { ButtonLink } from "@/components/ui/button"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import { dutyLabels } from "@/features/employees/dutyLabels"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/employees/EmployeeDetailPage.module.css"
import { getFriendlyErrorMessage } from "@/services/api"
import { DetailsPageActions } from "@/components/ui/details-page-actions"
import { Alert } from "@/components/ui/alert"
import { useEmployeeDetailQuery, useEmployeeEventsQuery, useArchiveEmployee, useUnarchiveEmployee, useDeleteEmployee } from "./hooks/use-employees"

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>()
  const employeeId = id ?? ""
  const navigate = useNavigate()
  const { data: employeeData, error: employeeError, isLoading: isEmployeeLoading } = useEmployeeDetailQuery(employeeId)
  const { data: employeeEvents, error: employeeEventsError, isLoading: isEmployeeEventsLoading } = useEmployeeEventsQuery(employeeId)

  const { mutate: archiveEmployee, isPending: isArchivePending, isError: isArchiveError, error: archiveError } = useArchiveEmployee()
  const { mutate: unarchiveEmployee, isPending: isUnarchivePending, isError: isUnarchiveError, error: unarchiveError } = useUnarchiveEmployee()
  const { mutate: deleteEmployee, isPending: isDeletePending, isError: isDeleteError, error: deleteError } = useDeleteEmployee()

  const handleArchiveToggle = () => {
    if (employeeData?.archivedAt) {
      unarchiveEmployee(employeeId)
    } else {
      archiveEmployee(employeeId)
    }
  }

  const handleEmployeeDelete = () => {
    if (globalThis.confirm("Tem certeza que deseja excluir este colaborador? Esta ação não pode ser desfeita.")) {
      deleteEmployee(employeeId)
    }
  }

  const mutationError = archiveError || unarchiveError || deleteError
  const isMutationError = isArchiveError || isUnarchiveError || isDeleteError

  if (isEmployeeLoading || isEmployeeEventsLoading) {
    return <PageLoading message="Carregando colaborador..." />
  }

  if (employeeError || employeeEventsError) {
    return (
      <div className={styles.page}>
        <ErrorCard
          description={getFriendlyErrorMessage(employeeError || employeeEventsError)}
          actionLabel="Voltar para listagem de colaboradores"
          onAction={() => navigate("/employees")} />
      </div>
    )
  }

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
        action={
          <ButtonLink to="/employees" variant="outline">
            Voltar para colaboradores
          </ButtonLink>
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

      <SectionCard
        title="Resumo do colaborador"
        description="Dados completos de cadastro, contato e status."
        headerAction={
          <DetailsPageActions
            data={employeeData}
            editTo={`/employees/edit/${employeeId}`}
            handleArchive={handleArchiveToggle}
            handleDelete={handleEmployeeDelete}
            isArchivePending={isArchivePending || isUnarchivePending}
            isDeletePending={isDeletePending}
          />
        }
      >
        {isMutationError && (
          <Alert variant="error">
            {getFriendlyErrorMessage(mutationError)}
          </Alert>
        )}
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
        {/* //TODO deve ter um jeito melhor de enviar a eventsPage */}
        <EventsTable
          eventsPage={employeeEvents ?? { content: [], page: { totalElements: 0, totalPages: 0, number: 0, size: 0 } }}
          loading={isEmployeeEventsLoading}
          error={employeeEventsError ? getFriendlyErrorMessage(employeeEventsError) : ""}
          variant="employeePage"
        />
      </SectionCard>
    </div>
  )
}
