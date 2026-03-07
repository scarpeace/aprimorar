import { useCallback, useEffect, useMemo, useRef, useState } from "react"
import { EmptyState } from "@/components/ui/empty-state"
import { StudentsListToolbar } from "@/features/students/components/StudentsListToolbar"
import { StudentsPagination } from "@/features/students/components/StudentsPagination"
import { StudentsTable } from "@/features/students/components/StudentsTable"
import { buildStudentFiltersLabel } from "@/features/students/utils/studentListUtils"
import type { StudentResponse } from "@/lib/schemas"
import { getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"
import styles from "@/features/students/pages/StudentsPage.module.css"
import { useNavigate } from "react-router-dom"

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

  const activeFiltersLabel = useMemo(() => buildStudentFiltersLabel(nameFilter, hideArchived), [hideArchived, nameFilter])

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
      <StudentsListToolbar
        hideArchived={hideArchived}
        nameFilterInput={nameFilterInput}
        activeFiltersLabel={activeFiltersLabel}
        onHideArchivedChange={setHideArchived}
        onNameFilterInputChange={setNameFilterInput}
      />

      {deleteError ? (
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
          {deleteError}
        </div>
      ) : null}

      <StudentsTable students={studentList} deletingId={deletingId} onToggleArchive={handleArchiveToggle} />

      <StudentsPagination
        page={page}
        size={size}
        totalPages={totalPages}
        totalElements={totalElements}
        isFirstPage={isFirstPage}
        isLastPage={isLastPage}
        onPageChange={setPage}
        onSizeChange={setSize}
      />

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
