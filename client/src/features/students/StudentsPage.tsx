import { useQuery } from "@tanstack/react-query"
import { useState } from "react"
import { Link } from "react-router-dom"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import styles from "@/features/students/StudentsPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { getFriendlyErrorMessage, studentsApi } from "@/services/api"

const STUDENTS_LIST_PARAMS = { page: 0, size: 20, sortBy: "name" }

export function StudentsPage() {
  const [hideArchived, setHideArchived] = useState(false)

  const {
    data: studentList = [],
    isLoading,
    isError,
    error,
    refetch,
  } = useQuery({
    queryKey: queryKeys.students.list(STUDENTS_LIST_PARAMS),
    queryFn: async () => {
      const studentsRes = await studentsApi.list(
        STUDENTS_LIST_PARAMS.page,
        STUDENTS_LIST_PARAMS.size,
        STUDENTS_LIST_PARAMS.sortBy
      )

      return studentsRes.content
    },
  })

  const visibleStudents = hideArchived ? studentList.filter((student) => !student.archivedAt) : studentList

  if (isLoading) {
    return <PageLoading message="Carregando alunos..." />
  }

  if (isError) {
    return (
      <div className={styles.page}>
        <PageHeader title="Alunos" description="Gerencie cadastros e matrículas." />
        <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetch} />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader
        action={
          <Link className="btn btn-success" to="/students/new">
            Novo aluno
          </Link>
        }
        description="Gerencie cadastros e matrículas."
        title="Alunos"
      >
        <div className="flex w-full flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <ListSearchInput
            placeholder="Buscar aluno por nome, email ou escola"
            ariaLabel="Buscar aluno"
          />
          <label className="app-text-muted flex items-center gap-2 text-sm">
            <input
              className="checkbox checkbox-sm"
              type="checkbox"
              checked={hideArchived}
              onChange={(event) => setHideArchived(event.target.checked)}
            />
            Ocultar arquivados
          </label>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <div className="overflow-x-auto">
          <table className="table w-full">
            <thead className="bg-base-200">
              <tr>
                <th className="app-th">Nome</th>
                <th className="app-th">Email</th>
                <th className="app-th">Escola</th>
                <th className="app-th">Ações</th>
                <th className="app-th">Status</th>
              </tr>
            </thead>
            <tbody>
              {visibleStudents.map((student) => (
                <tr key={student.id}>
                  <td>{student.name}</td>
                  <td>{student.email}</td>
                  <td>{student.school}</td>
                  <td>
                    <Link className="btn btn-ghost btn-sm" to={`/students/${student.id}`}>
                      Detalhes
                    </Link>
                  </td>
                  <td>
                    <span className={`badge ${student.archivedAt ? "badge-warning" : "badge-success"}`}>
                      {student.archivedAt ? "Arquivado" : "Ativo"}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {visibleStudents.length === 0 ? (
        <EmptyCard
          title="Nenhum aluno cadastrado"
          description="Quando você cadastrar o primeiro aluno, ele aparecerá na tabela acima."
          action={
            <Link className="btn btn-secondary" to="/students/new">
              Novo aluno
            </Link>
          }
        />
      ) : null}
    </div>
  )
}
