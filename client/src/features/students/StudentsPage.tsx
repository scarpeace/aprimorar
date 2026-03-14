import { useCallback, useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import type { StudentResponse } from "@/lib/schemas"
import { getFriendlyErrorMessage, studentsApi } from "@/services/api"
import styles from "@/features/students/StudentsPage.module.css"
import type { PageResponse } from "@/lib/schemas/page-response"
import { Badge } from "@/components/ui/badge"

export function StudentsPage() {
  const [studentList, setStudentList] = useState<StudentResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [hideArchived, setHideArchived] = useState(false)

  const loadStudents = useCallback(async () => {
    try {
      setError(null)
      setLoading(true)
      const studentsRes : PageResponse<StudentResponse> = await studentsApi.list(0, 20, "name")
      setStudentList(studentsRes.content)
    } catch (error) {
      console.error("Falha ao carregar alunos:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    loadStudents()
  }, [loadStudents])

  if (loading) {
    return <LoadingState message="Carregando alunos..." />
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Alunos</h1>
          <p className="text-sm text-gray-600">Gerencie cadastros e matrículas.</p>
        </div>
        <ErrorState
          title="Não foi possível carregar"
          description={error}
          actionLabel="Tentar novamente"
          onAction={loadStudents}
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Alunos</h1>
          <p className="text-sm text-gray-600">Gerencie cadastros e matrículas.</p>
          <label className="mt-3 flex items-center gap-2 text-sm text-gray-700">
            <input
              type="checkbox"
              checked={hideArchived}
              onChange={(e) => setHideArchived(e.target.checked)}
            />
            Ocultar arquivados
          </label>
        </div>
        <Button variant="success" asChild>
          <Link to="/students/new">Novo aluno</Link>
        </Button>
      </div>
      
      <div className={styles.tableWrap}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>Escola</TableHead>
              <TableHead>Ações</TableHead>
              <TableHead>Status</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {studentList.map((student) => (
              <TableRow key={student.id}>
                <TableCell>{student.name}</TableCell>
                <TableCell>{student.email}</TableCell>
                <TableCell>{student.school}</TableCell>
                <TableCell>
                  <Button variant="default" asChild>
                    <Link
                      className="text-sm font-medium text-blue-600 hover:underline"
                      to={`/students/${student.id}`}
                    >
                      Detalhes
                    </Link>
                    </Button>
                </TableCell>
                <TableCell>
                  {student.archivedAt ? <Badge variant="warn">Arquivado</Badge> : <Badge variant="success">Ativo</Badge>}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {studentList.length === 0 ? (
        <EmptyState
          title="Nenhum aluno cadastrado"
          description="Quando você cadastrar o primeiro aluno, ele aparecerá na tabela acima."
          actionLabel="Novo aluno"
        />
      ) : null}
    </div>
  )
}
