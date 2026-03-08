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
import { getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"
import styles from "@/features/students/StudentsPage.module.css"

export function StudentsPage() {
  const [studentList, setStudentList] = useState<StudentResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleteError, setDeleteError] = useState<string | null>(null)
  const [deletingId, setDeletingId] = useState<string | null>(null)
  const [hideArchived, setHideArchived] = useState(false)

  const loadStudents = useCallback(async () => {
    try {
      setError(null)
      setLoading(true)
      const studentsRes = await studentsApi.list(0, 20, "name", !hideArchived)
      const studentsPage: PageResponse<StudentResponse> = studentsRes.data
      setStudentList(studentsPage.content)
    } catch (error) {
      console.error("Falha ao carregar alunos:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }, [hideArchived])

  useEffect(() => {
    loadStudents()
  }, [loadStudents])

  const handleArchiveToggle = async (student: StudentResponse) => {
    const isArchived = !!student.archivedAt
    const verb = isArchived ? "Reativar" : "Arquivar"
    const confirmText = isArchived
      ? `Reativar aluno "${student.name}"?`
      : `Arquivar aluno "${student.name}"?`

    if (!window.confirm(confirmText)) {
      return
    }

    try {
      setDeleteError(null)
      setDeletingId(student.id)

      if (isArchived) {
        await studentsApi.unarchive(student.id)
      } else {
        await studentsApi.archive(student.id)
      }

      await loadStudents()
    } catch (error) {
      console.error(`Falha ao ${verb.toLowerCase()} aluno:`, error)
      setDeleteError(getFriendlyErrorMessage(error))
    } finally {
      setDeletingId(null)
    }
  }

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
        <Button asChild type="button">
          <Link to="/students/new">Novo aluno</Link>
        </Button>
      </div>

      {deleteError ? (
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
          {deleteError}
        </div>
      ) : null}

      <div className={styles.tableWrap}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>Idade</TableHead>
              <TableHead>Escola</TableHead>
              <TableHead>Arquivado</TableHead>
              <TableHead>Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {studentList.map((student) => (
              <TableRow key={student.id}>
                <TableCell>{student.name}</TableCell>
                <TableCell>{student.email}</TableCell>
                <TableCell>{student.age}</TableCell>
                <TableCell>{student.school}</TableCell>
                <TableCell>{student.archivedAt ? "Sim" : "Não"}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <Link
                      className="text-sm font-medium text-blue-600 hover:underline"
                      to={`/students/${student.id}`}
                    >
                      Detalhes
                    </Link>
                    <Button
                      type="button"
                      variant={student.archivedAt ? "outline" : "destructive"}
                      size="sm"
                      onClick={() => handleArchiveToggle(student)}
                      disabled={deletingId === student.id}
                    >
                      {deletingId === student.id
                        ? "Salvando..."
                        : student.archivedAt
                          ? "Reativar"
                          : "Arquivar"}
                    </Button>
                  </div>
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
