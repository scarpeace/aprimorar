import { useQuery } from "@tanstack/react-query"
import type { ReactNode } from "react"
import { GraduationCap } from "lucide-react"
import { Link, useParams } from "react-router-dom"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/students/StudentDetailPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { eventsApi, getFriendlyErrorMessage, studentsApi } from "@/services/api"

const STUDENT_EVENTS_PARAMS = { page: 0, size: 100, sortBy: "startDate" }

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const studentId = id ?? ""

  const studentQuery = useQuery({
    queryKey: queryKeys.students.detail(studentId),
    queryFn: () => studentsApi.getById(studentId),
    enabled: Boolean(id),
  })

  const studentEventsQuery = useQuery({
    queryKey: queryKeys.events.byStudent(studentId, STUDENT_EVENTS_PARAMS),
    queryFn: () =>
      eventsApi.listByStudent(
        studentId,
        STUDENT_EVENTS_PARAMS.page,
        STUDENT_EVENTS_PARAMS.size,
        STUDENT_EVENTS_PARAMS.sortBy
      ),
    enabled: Boolean(id),
  })

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do aluno não informado." />
      </div>
    )
  }

  const refetchAll = async () => {
    await Promise.all([studentQuery.refetch(), studentEventsQuery.refetch()])
  }

  if (studentQuery.isLoading || studentEventsQuery.isLoading) {
    return <PageLoading message="Carregando aluno..." />
  }

  if (studentQuery.isError || studentEventsQuery.isError) {
    const queryError = studentQuery.error ?? studentEventsQuery.error

    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(queryError)} onAction={refetchAll} />
      </div>
    )
  }

  const student = studentQuery.data

  if (!student) {
    return (
      <div className={styles.page}>
        <EmptyCard title="Aluno não encontrado" description="Não encontramos os dados deste aluno." />
      </div>
    )
  }

  const linkedEvents = studentEventsQuery.data?.content ?? []
  const studentEventsCount = studentEventsQuery.data?.page.totalElements ?? 0

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: student.name },
    { label: "CPF", value: student.cpf },
    { label: "E-mail", value: student.email },
    { label: "Idade", value: String(student.age) },
    { label: "Contato", value: student.contact },
    { label: "Data de nascimento", value: student.birthdate },
    { label: "Data de matrícula", value: student.createdAt },
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
          <Link className="btn btn-outline" to="/students">
            Voltar para alunos
          </Link>
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

      <SectionCard title="Resumo do aluno" description="Dados de aluno, responsável e endereço em um único resumo.">
        <div className={styles.summaryGrid}>
          {summaryItems.map((item) => (
            <SummaryItem key={item.label} label={item.label} value={item.value} />
          ))}
        </div>
      </SectionCard>

      <SectionCard
        title="Eventos vinculados"
        description={`Total de eventos vinculados a este aluno: ${studentEventsCount}`}
      >
        {linkedEvents.length === 0 ? (
          <p className="text-sm app-text-muted">Este aluno ainda não possui eventos vinculados.</p>
        ) : (
          <div className="app-table-wrap">
            <EventsTable variant="studentPage" events={linkedEvents} />
          </div>
        )}
      </SectionCard>
    </div>
  )
}
