import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import { SummaryField } from "@/components/ui/summary-field"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { GraduationCap } from "lucide-react"
import { eventContentLabels, type EventResponse } from "@/lib/schemas/event"
import type { StudentResponse } from "@/lib/schemas/student"
import { eventsApi, getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import styles from "@/features/students/StudentDetailPage.module.css"

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
        eventsApi.listByStudent(id, 0, 100, "startDateTime"),
      ])

      const eventsPage: PageResponse<EventResponse> = eventsRes.data

      setStudent(studentRes.data)
      setLinkedEvents(eventsPage.content)
      setStudentEventsCount(eventsPage.totalElements)
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

  const parentName = student.parent?.name ?? "-"
  const parentEmail = student.parent?.email ?? "-"
  const parentContact = student.parent?.contact ?? "-"
  const parentCpf = student.parent?.cpf ?? "-"

  const address = student.address
    ? `${student.address.street}, ${student.address.number} - ${student.address.district}, ${student.address.city}/${student.address.state}`
    : "-"
  const addressComplement = student.address?.complement ?? "-"
  const addressZip = student.address?.zip ?? "-"

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
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Nome completo" value={student.name} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="CPF" value={student.cpf} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="E-mail" value={student.email} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Idade" value={String(student.age)} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Contato" value={student.contact} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Data de nascimento" value={student.birthdate} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Data de matrícula" value={student.createdAt} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Escola" value={student.school} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Status" value={student.archivedAt ? "Arquivado" : "Ativo"} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Responsável" value={parentName} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="E-mail do responsável" value={parentEmail} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Contato do responsável" value={parentContact} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="CPF do responsável" value={parentCpf} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Endereço" value={address} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="Complemento" value={addressComplement} />
            <SummaryField className={styles.summaryItem} labelClassName={styles.summaryLabel} valueClassName={styles.summaryValue} label="CEP" value={addressZip} />
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
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Título</TableHead>
                    <TableHead>Conteúdo</TableHead>
                    <TableHead>Início</TableHead>
                    <TableHead>Fim</TableHead>
                    <TableHead>Colaborador</TableHead>
                    <TableHead>Preço</TableHead>
                    <TableHead>Ações</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {linkedEvents.map((event) => (
                    <TableRow key={event.id}>
                      <TableCell>{event.title}</TableCell>
                      <TableCell>{eventContentLabels[event.content]}</TableCell>
                      <TableCell>{event.startDateTime}</TableCell>
                      <TableCell>{event.endDateTime}</TableCell>
                      <TableCell>{event.employeeName}</TableCell>
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
