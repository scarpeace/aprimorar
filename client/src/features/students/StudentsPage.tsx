import { useState, useEffect } from "react"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { Pagination } from "@/components/ui/pagination"
import { ButtonLink } from "@/components/ui/button"
import styles from "@/features/students/StudentsPage.module.css"
import { getFriendlyErrorMessage } from "@/services/api"
import { useStudentsQuery } from "./hooks/use-students"
import { useDebounce } from "@/hooks/use-debounce"

export function StudentsPage() {
  const [currentPage, setCurrentPage] = useState(0)
  const [searchTerm, setSearchTerm] = useState("")
  const debouncedSearchTerm = useDebounce(searchTerm, 500)
  const pageSize = 10

  const {
    data: response,
    isLoading,
    isError,
    error,
    refetch,
  } = useStudentsQuery(currentPage, pageSize, debouncedSearchTerm)

  // Reset pagination when search changes
  useEffect(() => {
    setCurrentPage(0)
  }, [debouncedSearchTerm])

  const studentList = response?.content ?? []
  const pageInfo = response?.page

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

  const totalElements = pageInfo?.totalElements ?? 0
  const totalPages = pageInfo?.totalPages ?? 0

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
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ButtonLink className="sm:ml-auto" to="/students/new" variant="success">
            Novo aluno
          </ButtonLink>
        </div>
      </PageHeader>

      {/* TODO transformar isso no StudentsTable */}
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
              {studentList.map((student) => (
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

      <Pagination
        currentPage={currentPage}
        totalElements={totalElements}
        totalPages={totalPages}
        currentElementsCount={studentList.length}
        itemName="alunos"
        onPageChange={setCurrentPage}
      />

      {studentList.length === 0 ? (
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
