import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import { SummaryField } from "@/components/ui/summary-field"
import { GraduationCap } from "lucide-react"
import type { EventResponse } from "@/lib/schemas/event"
import type { StudentResponse } from "@/lib/schemas/student"
import { eventsApi, getFriendlyErrorMessage, studentsApi } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import styles from "@/features/students/StudentDetailPage.module.css"
import type { PageResponse } from "@/lib/schemas/page-response"
import { EventsTable } from "@/components/ui/events-table"

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [student, setStudent] = useState<StudentResponse | null>(null)
  const [linkedEvents, setLinkedEvents] = useState<EventResponse[]>([])
  const [studentEventsCount, setStudentEventsCount] = useState(0)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadStudentData = useCallback(async () => {
    if (!id) {
      setError("ID do aluno não informado.")
      return
    }

    try {
      setLoading(true)
      setError(null)

      const [studentRes, eventsRes] = await Promise.all([
        studentsApi.getById(id),
        eventsApi.listByStudent(id, 0, 100, "startDate"),
      ])

      const eventsPage: PageResponse<EventResponse> = eventsRes

      setStudent(studentRes)
      setLinkedEvents(eventsRes.content)
      setStudentEventsCount(eventsPage.page.totalElements)
    } catch (loadError) {
      console.error("Falha ao carregar aluno:", loadError)
      setError(getFriendlyErrorMessage(loadError))
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    loadStudentData()
  }, [loadStudentData])

  if (loading) return <LoadingState message="Carregando aluno..." />

  if (error) {
    return (
      <div className={styles.page}>
        <ErrorState
          title="Não foi possível carregar"
          description={error}
          actionLabel="Tentar novamente"
          onAction={loadStudentData}
        />
      </div>
    )
  }

  if (!student) {
    return (
      <div className={styles.page}>
        <EmptyState title="Aluno não encontrado" description="Não encontramos os dados deste aluno." />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-blue-100">
            <GraduationCap className="h-6 w-6 text-blue-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Detalhes do aluno</h1>
            <p className="text-sm text-gray-500">Veja e gerencie as informações do aluno</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/students">← Voltar para alunos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Resumo do aluno</CardTitle>
          <CardDescription>Dados de aluno, responsável e endereço em um único resumo.</CardDescription>
        </CardHeader>
        <CardContent>
          <div className={styles.summaryGrid}>
            <SummaryField title="Nome completo">{student.name}</SummaryField>
            <SummaryField title="CPF">{student.cpf}</SummaryField>
            <SummaryField title="E-mail">{student.email}</SummaryField>
            <SummaryField title="Idade">{String(student.age)}</SummaryField>
            <SummaryField title="Contato">{student.contact}</SummaryField>
            <SummaryField title="Data de nascimento">{student.birthdate}</SummaryField>
            <SummaryField title="Data de matrícula">{student.createdAt}</SummaryField>
            <SummaryField title="Escola">{student.school}</SummaryField>
            <SummaryField title="Status">{student.archivedAt ? "Arquivado" : "Ativo"}</SummaryField>
            <SummaryField title="Responsável">{student.parent.name}</SummaryField>
            <SummaryField title="E-mail do responsável">{student.parent.email}</SummaryField>
            <SummaryField title="Contato do responsável">{student.parent.contact}</SummaryField>
            <SummaryField title="CPF do responsável">{student.parent.cpf}</SummaryField>
            <SummaryField title="Endereço">{student.address.street}</SummaryField>

            {student.address.complement != null ?
            <SummaryField title="Complemento">{student.address.complement ? student.address.complement : null}</SummaryField>
              :"Sem complemento"
            }
            <SummaryField title="CEP">{student.address.zip}</SummaryField>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Eventos vinculados</CardTitle>
          <CardDescription>Total de eventos vinculados a este aluno: {studentEventsCount}</CardDescription>
        </CardHeader>
        <CardContent>
          {linkedEvents.length === 0 ? (
            <p className="text-sm text-muted-foreground">Este aluno ainda não possui eventos vinculados.</p>
          ) : (
            <div className={styles.tableWrap}>
              <EventsTable variant="studentPage" events={linkedEvents} />
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
