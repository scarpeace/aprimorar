import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { GraduationCap } from "lucide-react"
import { eventContentLabels, type EventResponse, type StudentResponse } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"
import { useEffect, useState } from "react"
import styles from "@/features/students/StudentDetailPage.module.css"

function SummaryField({ label, value }: { label: string; value: string }) {
  return (
    <div className={styles.summaryItem}>
      <p className={styles.summaryLabel}>{label}</p>
      <p className={styles.summaryValue}>{value}</p>
    </div>
  )
}

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [student, setStudent] = useState<StudentResponse | null>(null)
  const [linkedEvents, setLinkedEvents] = useState<EventResponse[]>([])
  const [studentEventsCount, setStudentEventsCount] = useState(0)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!id) {
      setError("ID do aluno não informado.")
      setLoading(false)
      return
    }

    const fetchStudent = async () => {
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
      } catch (error) {
        console.error("Falha ao carregar aluno:", error)
        setError(getFriendlyErrorMessage(error))
      } finally {
        setLoading(false)
      }
    }

    fetchStudent()
  }, [id])

  if (loading) return <div>Carregando...</div>
  if (error) return <div>{error}</div>
  if (!student) return <div>Aluno não encontrado.</div>

  const address = student.address
    ? `${student.address.street}, ${student.address.number} - ${student.address.district}, ${student.address.city}/${student.address.state}`
    : "-"

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
          <Link to="/students">
            ← Voltar para alunos
          </Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Resumo do aluno</CardTitle>
          <CardDescription>Dados completos de cadastro, contato e vínculo.</CardDescription>
        </CardHeader>
        <CardContent>
          <div className={styles.summaryGrid}>
            <SummaryField label="Nome completo" value={student.name} />
            <SummaryField label="CPF" value={student.cpf} />
            <SummaryField label="E-mail" value={student.email} />
            <SummaryField label="Idade" value={String(student.age)} />
            <SummaryField label="Contato" value={student.contact} />
            <SummaryField label="Data de nascimento" value={student.birthdate} />
            <SummaryField label="Data de matrícula" value={student.createdAt} />
            <SummaryField label="Escola" value={student.school} />
            <SummaryField label="Responsável" value={student.parent?.name ?? "-"} />
            <SummaryField label="Endereço" value={address} />
            <SummaryField label="Status" value={student.archivedAt ? "Arquivado" : "Ativo"} />
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
