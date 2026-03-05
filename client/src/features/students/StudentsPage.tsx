import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
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

  const loadStudents = async () => {
    try {
      setError(null)
      setLoading(true)
      const studentsRes = await studentsApi.list()
      const studentsPage: PageResponse<StudentResponse> = studentsRes.data
      setStudentList(studentsPage.content)
    } catch (error) {
      console.error("Falha ao carregar alunos:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadStudents()
  }, [])

  const handleDelete = async (student: StudentResponse) => {
    if (!window.confirm(`Excluir aluno "${student.name}"? Essa acao nao pode ser desfeita.`)) {
      return
    }

    try {
      setDeleteError(null)
      setDeletingId(student.id)
      await studentsApi.delete(student.id)
      setStudentList((prev) => prev.filter((item) => item.id !== student.id))
    } catch (error) {
      console.error("Falha ao excluir aluno:", error)
      setDeleteError(getFriendlyErrorMessage(error))
    } finally {
      setDeletingId(null)
    }
  }

  if (loading) {
    return <div>Carregando...</div>
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Alunos</h1>
          <p className="text-sm text-gray-600">Gerencie cadastros e matriculas.</p>
        </div>
        <EmptyState
          title="Nao foi possivel carregar"
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
          <p className="text-sm text-gray-600">Gerencie cadastros e matriculas.</p>
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
              <TableHead>Ativo</TableHead>
              <TableHead>Acoes</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {studentList.map((student) => (
              <TableRow key={student.id}>
                <TableCell>{student.name}</TableCell>
                <TableCell>{student.email}</TableCell>
                <TableCell>{student.age}</TableCell>
                <TableCell>{student.school}</TableCell>
                <TableCell>{student.active ? "Sim" : "Nao"}</TableCell>
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
                      variant="destructive"
                      size="sm"
                      onClick={() => handleDelete(student)}
                      disabled={deletingId === student.id}
                    >
                      {deletingId === student.id ? "Excluindo..." : "Excluir"}
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
          description="Quando voce cadastrar o primeiro aluno, ele aparecera na tabela acima."
          actionLabel="Novo aluno"
        />
      ) : null}
    </div>
  )
}
