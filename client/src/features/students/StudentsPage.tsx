import { useCallback, useEffect, useMemo, useRef, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { Input } from "@/components/ui/input"
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

const SEARCH_DEBOUNCE_MS = 350

export function StudentsPage() {
  const navigate = useNavigate()
  const [studentList, setStudentList] = useState<StudentResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleteError, setDeleteError] = useState<string | null>(null)
  const [deletingId, setDeletingId] = useState<string | null>(null)
  const [hideArchived, setHideArchived] = useState(true)
  const [nameFilterInput, setNameFilterInput] = useState("")
  const [nameFilter, setNameFilter] = useState("")
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(20)
  const [sortBy] = useState("name")
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [isFirstPage, setIsFirstPage] = useState(true)
  const [isLastPage, setIsLastPage] = useState(true)
  const requestIdRef = useRef(0)

  const activeFiltersLabel = useMemo(() => {
    const filters: string[] = []

    if (nameFilter) {
      filters.push(`nome: "${nameFilter}"`)
    }

    filters.push(hideArchived ? "somente ativos" : "inclui arquivados")

    return filters.join(" • ")
  }, [hideArchived, nameFilter])

  useEffect(() => {
    const timeout = window.setTimeout(() => {
      setNameFilter(nameFilterInput.trim())
    }, SEARCH_DEBOUNCE_MS)

    return () => {
      window.clearTimeout(timeout)
    }
  }, [nameFilterInput])

  useEffect(() => {
    setPage(0)
  }, [hideArchived, nameFilter, size, sortBy])

  const loadStudents = useCallback(async () => {
    const requestId = requestIdRef.current + 1
    requestIdRef.current = requestId

    try {
      setError(null)
      setLoading(true)

      const studentsRes = await studentsApi.list(page, size, sortBy, !hideArchived, nameFilter)
      const studentsPage: PageResponse<StudentResponse> = studentsRes.data

      if (requestId !== requestIdRef.current) {
        return
      }

      if (page > 0 && studentsPage.totalPages > 0 && page >= studentsPage.totalPages) {
        setPage(studentsPage.totalPages - 1)
        return
      }

      setStudentList(studentsPage.content)
      setTotalElements(studentsPage.totalElements)
      setTotalPages(studentsPage.totalPages)
      setIsFirstPage(studentsPage.first)
      setIsLastPage(studentsPage.last)
    } catch (error) {
      if (requestId !== requestIdRef.current) {
        return
      }

      console.error("Falha ao carregar alunos:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      if (requestId === requestIdRef.current) {
        setLoading(false)
      }
    }
  }, [hideArchived, nameFilter, page, size, sortBy])

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
    return <div>Carregando alunos...</div>
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Alunos</h1>
          <p className="text-sm text-gray-600">Gerencie cadastros e matrículas.</p>
        </div>
        <EmptyState
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

      <div className={styles.filters}>
        <div className={styles.filterField}>
          <label className={styles.filterLabel} htmlFor="student-name-filter">
            Buscar por nome
          </label>
          <Input
            id="student-name-filter"
            type="search"
            value={nameFilterInput}
            onChange={(event) => setNameFilterInput(event.target.value)}
            placeholder="Ex: Maria"
          />
        </div>
        <div className={styles.meta}>Filtros: {activeFiltersLabel}</div>
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
              <TableHead>Status</TableHead>
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
                <TableCell>{student.archivedAt ? "Arquivado" : "Ativo"}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <Link
                      className="text-sm font-medium text-blue-600 hover:underline"
                      to={`/students/${student.id}`}
                    >
                      Detalhes
                    </Link>
                    <Link
                      className="text-sm font-medium text-blue-600 hover:underline"
                      to={`/students/${student.id}/edit`}
                    >
                      Editar
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

      {totalElements > 0 ? (
        <div className={styles.pagination}>
          <div className={styles.meta}>
            Página {page + 1} de {Math.max(totalPages, 1)} • {totalElements} aluno(s)
          </div>
          <div className={styles.paginationActions}>
            <label className={styles.pageSizeLabel} htmlFor="students-page-size">
              Itens por página
            </label>
            <select
              id="students-page-size"
              className={styles.pageSizeSelect}
              value={size}
              onChange={(event) => setSize(Number(event.target.value))}
            >
              <option value={10}>10</option>
              <option value={20}>20</option>
              <option value={50}>50</option>
            </select>
            <Button type="button" variant="outline" onClick={() => setPage((prev) => prev - 1)} disabled={isFirstPage}>
              Anterior
            </Button>
            <Button type="button" variant="outline" onClick={() => setPage((prev) => prev + 1)} disabled={isLastPage}>
              Próxima
            </Button>
          </div>
        </div>
      ) : null}

      {studentList.length === 0 ? (
        <EmptyState
          title="Nenhum aluno cadastrado"
          description="Quando você cadastrar o primeiro aluno, ele aparecerá na tabela acima."
          actionLabel="Novo aluno"
          onAction={() => navigate("/students/new")}
        />
      ) : null}
    </div>
  )
}
