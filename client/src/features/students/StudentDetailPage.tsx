import { useMutation, useQueries, useQueryClient } from "@tanstack/react-query"
import type { ReactNode } from "react"
import { GraduationCap } from "lucide-react"
import { useNavigate, useParams } from "react-router-dom"
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
import { eventsApi, getFriendlyErrorMessage, studentsApi } from "@/services/api"
import { DetailsPageActions } from "@/components/ui/details-page-actions"


export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const studentId = id ?? ""
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const [studentQuery, eventsQuery] = useQueries({
    queries: [
      {
        queryKey: queryKeys.studentDetail(studentId),
        queryFn: () => studentsApi.getById(studentId),
        enabled: Boolean(id),
      },
      {
        queryKey: queryKeys.eventsByStudent(studentId),
        queryFn: () => eventsApi.listByStudent(studentId),
        enabled: Boolean(id),
      },
    ],
  })

  const {
    data: studentData,
    error: studentError,
    isLoading: isStudentLoading,
    refetch: refetchStudent,
  } = studentQuery;

  const {
    data: eventsData,
    error: eventsError,
    isLoading: isEventsLoading,
    refetch: refetchEvents
  } = eventsQuery;

  const {
    mutate: deleteStudentMutation,
    error: deleteStudentMutationError,
    isPending: isDeleteStudentMutationPending,
  } = useMutation({
    mutationFn: () => studentsApi.delete(studentId),
    onSuccess: async () => {
      // Remove o aluno específico do cache para evitar que o invalidQueries tente buscá-lo novamente e dê 404
      queryClient.removeQueries({ queryKey: [...queryKeys.students, studentId] })

      await Promise.all([
        queryClient.invalidateQueries({ queryKey: queryKeys.students }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
        queryClient.invalidateQueries({ queryKey: queryKeys.eventsByStudent(studentId) }),
      ])

      globalThis.alert("Aluno excluído com sucesso. ")
      navigate("/students")
    },
  })

  const {
    mutate: archiveStudentMutation,
    error: archiveStudentMutationError,
    isPending: isArchiveStudentMutationPending,
  } = useMutation({
    mutationFn: () =>
      studentData?.archivedAt ? studentsApi.unarchive(studentId) : studentsApi.archive(studentId),
    onSuccess: async () => {
      await Promise.all([
        queryClient.invalidateQueries({ queryKey: [...queryKeys.students, studentId] }),
        queryClient.invalidateQueries({ queryKey: queryKeys.students }),
        queryClient.invalidateQueries({ queryKey: queryKeys.events }),
        queryClient.invalidateQueries({ queryKey: queryKeys.dashboard }),
      ])
    },
  })

  const refetchAll = async () => {
    await Promise.all([refetchStudent(), refetchEvents()])
  }

  const handleStudentDelete = () => {
    if (!globalThis.confirm("Deseja realmente excluir este aluno? Todos os eventos com esse aluno serão impactados no sistema.")) {
      return
    }
    if (!globalThis.confirm("TEM CERTEZA QUE REALMENTE QUER EXCLUIR ESTE ALUNO? ESSA AÇÃO NÃO PODE SER DESFEITA")) {
      return
    }
    deleteStudentMutation()
  }

  if (isStudentLoading || isEventsLoading) {
    return <PageLoading message="Carregando aluno..." />
  }

  //QUERIES ERRORS
  if (studentError || eventsError) {
    const queryError = studentError ?? eventsError

    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(queryError)} onAction={refetchAll} />
      </div>
    )
  }

  //MUTATION ERRORS
  if (deleteStudentMutationError || archiveStudentMutationError) {
    const mutationError = deleteStudentMutationError ?? archiveStudentMutationError

    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(mutationError)} onAction={refetchAll} />
      </div>
    )
  }

  if (!studentData) {
    return (
      <div className={styles.page}>
        <EmptyCard title="Aluno não encontrado" description="Não encontramos os dados deste aluno." />
      </div>
    )
  }

  const studentEventsCount = eventsData?.page.totalElements ?? 0

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: studentData.name },
    { label: "CPF", value: studentData.cpf },
    { label: "E-mail", value: studentData.email },
    { label: "Idade", value: studentData.age },
    { label: "Contato", value: studentData.contact },
    { label: "Data de nascimento", value: studentData.birthdate },
    { label: "Data de matrícula", value: formatDateShortYear(studentData.createdAt) },
    { label: "Escola", value: studentData.school },
    { label: "Status", value: studentData.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Responsável", value: studentData.parent.name },
    { label: "E-mail do responsável", value: studentData.parent.email },
    { label: "Contato do responsável", value: studentData.parent.contact },
    { label: "CPF do responsável", value: studentData.parent.cpf },
    { label: "Endereço", value: studentData.address.street },
    { label: "Complemento", value: studentData.address.complement ?? "Sem complemento" },
    { label: "CEP", value: studentData.address.zip },
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
            data={studentData}
            editTo={`/students/edit/${studentData.id}`}
            handleArchive={archiveStudentMutation}
            handleDelete={handleStudentDelete}
            isArchivePending={isArchiveStudentMutationPending}
            isDeletePending={isDeleteStudentMutationPending}
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
        {eventsData?.page.totalElements === 0 ? (
          <p className="text-sm app-text-muted">Este aluno ainda não possui eventos vinculados.</p>
        ) : (
          <div className="app-table-wrap">

            {/*TODO: deve ter um jeito melhor de enviar a eventsPage */}
            <EventsTable
              variant="studentPage"
              eventsPage={eventsData!}
              loading={isEventsLoading}
              error={eventsError ? getFriendlyErrorMessage(eventsError) : ""} />
          </div>
        )}
      </SectionCard>
    </div >
  )
}
