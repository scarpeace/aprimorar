import type { ReactNode } from "react"
import { GraduationCap } from "lucide-react"
import { useParams } from "react-router-dom"
import { ButtonLink } from "@/components/ui/button"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/students/StudentDetailPage.module.css"
import { formatDateShortYear } from "@/lib/shared/formatter"
import { queryKeys } from "@/lib/query/queryKeys"
import { getFriendlyErrorMessage, eventsApi } from "@/services/api"
import { DetailsPageActions } from "@/components/ui/details-page-actions"
import { useStudentDetailQuery, useArchiveStudent, useUnarchiveStudent, useDeleteStudent } from "./hooks/use-students"
import { useQuery } from "@tanstack/react-query"

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const studentId = id ?? ""

  const { data: student, isLoading: isStudentLoading, isError: isStudentError, error: studentError, refetch: refetchStudent } = useStudentDetailQuery(studentId)

  const eventsQuery = useQuery({
    queryKey: queryKeys.eventsByStudent(studentId),
    queryFn: () => eventsApi.listByStudent(studentId),
    enabled: Boolean(id),
  })

  const {
    data: events,
    error: eventsError,
    isLoading: isEventsLoading,
    refetch: refetchEvents
  } = eventsQuery;

  const { mutate: deleteStudent, isPending: isDeleting, error: deleteError } = useDeleteStudent()
  const { mutate: archiveStudent, isPending: isArchiving } = useArchiveStudent()
  const { mutate: unarchiveStudent, isPending: isUnarchiving } = useUnarchiveStudent()

  const handleArchiveToggle = () => {
    if (!student) return
    if (student.archivedAt) {
      unarchiveStudent(studentId)
    } else {
      if (!globalThis.confirm("Deseja realmente arquivar este aluno?")) return
      archiveStudent(studentId)
    }
  }

  const handleStudentDelete = () => {
    if (!globalThis.confirm("Deseja realmente excluir este aluno? Todos os eventos com esse aluno serão impactados no sistema.")) {
      return
    }
    if (!globalThis.confirm("TEM CERTEZA QUE REALMENTE QUER EXCLUIR ESTE ALUNO? ESSA AÇÃO NÃO PODE SER DESFEITA")) {
      return
    }
    deleteStudent(studentId)
  }

  const refetchAll = async () => {
    await Promise.all([refetchStudent(), refetchEvents()])
  }

  if (isStudentLoading || isEventsLoading) {
    return <PageLoading message="Carregando aluno..." />
  }

  const mutationError = deleteError
  if (isStudentError || eventsError || mutationError) {
    const error = studentError ?? eventsError ?? mutationError

    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetchAll} />
      </div>
    )
  }

  if (!student) {
    return (
      <div className={styles.page}>
        <EmptyCard title="Aluno não encontrado" description="Não encontramos os dados deste aluno." />
      </div>
    )
  }

  const studentEventsCount = events?.page.totalElements ?? 0

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: student.name },
    { label: "CPF", value: student.cpf },
    { label: "E-mail", value: student.email },
    { label: "Idade", value: student.age },
    { label: "Contato", value: student.contact },
    { label: "Data de nascimento", value: student.birthdate },
    { label: "Data de matrícula", value: formatDateShortYear(student.createdAt) },
    { label: "Escola", value: student.school },
    { label: "Status", value: student.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Responsável", value: student.parent.name },
    { label: "E-mail do responsável", value: student.parent.email },
    { label: "Contato do responsável", value: student.parent.contact },
    { label: "CPF do responsável", value: student.parent.cpf },
    { label: "Endereço", value: student.address.street },
    { label: "Complemento", value: student.address.complement ?? "Sem complemento" },
    { label: "CEP", value: student.address.zip },
  ]

  return (
    <div className={styles.page}>
      <PageHeader
        action={
          <ButtonLink to="/students" variant="outline">
            Voltar para alunos
          </ButtonLink>
        }
        description="Veja e gerencie as informações do aluno"
        leading={
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-primary/15">
            <GraduationCap className="h-6 w-6 text-primary" />
          </div>
        }
        title="Detalhes do aluno"
        titleClassName="text-2xl font-bold app-text"
      />

      <SectionCard
        title="Resumo do aluno"
        description="Dados de aluno, responsável e endereço em um único resumo."
        headerAction={
          <DetailsPageActions
            data={student}
            editTo={`/students/edit/${student.id}`}
            handleArchive={handleArchiveToggle}
            handleDelete={handleStudentDelete}
            isArchivePending={isArchiving || isUnarchiving}
            isDeletePending={isDeleting}
          />
        }
      >
        <div className={styles.summaryGrid}>
          {
            summaryItems.map((item) => (
              <SummaryItem key={item.label} label={item.label} value={item.value} />
            ))
          }
        </div>
      </SectionCard >

      {/* EVENTOS DO ALUNO */}

      <SectionCard
        title="Eventos vinculados"
        description={`Total de eventos vinculados a este aluno: ${studentEventsCount}`}
      >
        {studentEventsCount === 0 ? (
          <p className="text-sm app-text-muted">Este aluno ainda não possui eventos vinculados.</p>
        ) : (
          <div className="app-table-wrap">
            <EventsTable
              variant="studentPage"
              eventsPage={events!}
              loading={isEventsLoading}
              error={eventsError ? getFriendlyErrorMessage(eventsError) : ""} />
          </div>
        )}
      </SectionCard>
    </div >
  )
}
