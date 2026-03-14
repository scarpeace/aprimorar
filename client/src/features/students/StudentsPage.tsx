import { useQuery } from "@tanstack/react-query"
import { useState } from "react"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { ButtonLink } from "@/components/ui/button"
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
        description="Gerencie cadastros e matrículas."
        title="Alunos"
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar aluno por nome, email ou escola"
            ariaLabel="Buscar aluno"
          />
          <label className="app-text-muted flex items-center gap-2 whitespace-nowrap text-sm">
            <input
              className="checkbox checkbox-sm"
              type="checkbox"
              checked={hideArchived}
              onChange={(event) => setHideArchived(event.target.checked)}
            />
            Ocultar arquivados
          </label>
          <ButtonLink className="sm:ml-auto" to="/students/new" variant="success">
            Novo aluno
          </ButtonLink>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <div className="overflow-x-auto">
          <table className="table table-zebra w-full">
            <thead className="bg-base-200/90">
              <tr>
                <th className="app-th">Nome</th>
                <th className="app-th hidden lg:table-cell">Email</th>
                <th className="app-th">Escola</th>
                <th className="app-th">Ações</th>
                <th className="app-th-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {visibleStudents.map((student) => (
                <tr className="transition-colors hover:bg-base-200/70" key={student.id}>
                  <td>{student.name}</td>
                  <td className="hidden lg:table-cell">{student.email}</td>
                  <td> {student.school}</td>
                  <td>
                    <ButtonLink size="sm" to={`/students/${student.id}`} variant="outline">
                      Detalhes
                    </ButtonLink>
                  </td>
                  <td className="text-center">
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
            <ButtonLink to="/students/new" variant="secondary">
              Novo aluno
            </ButtonLink>
          }
        />
      ) : null}
    </div>
  )
}
