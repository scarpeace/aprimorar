import { useState } from "react"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { ButtonLink } from "@/components/ui/button"
import { dutyLabels } from "@/features/employees/dutyLabels"
import styles from "@/features/employees/EmployeesPage.module.css"
import { getFriendlyErrorMessage } from "@/services/api"
import { useEmployeesQuery } from "./hooks/use-employees"

export function EmployeesPage() {
  const [currentPage, setCurrentPage] = useState(0)
  const pageSize = 10

  const {
    data: response,
    isLoading,
    isError,
    error,
    refetch,
  } = useEmployeesQuery(currentPage, pageSize)

  const employeeList = response?.content ?? []
  const pageInfo = response?.page

  if (isLoading) {
    return <PageLoading message="Carregando colaboradores..." />
  }

  if (isError) {
    return (
      <div className={styles.page}>
        <PageHeader title="Colaboradores" description="Gerencie professores e equipe." />
        <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetch} />
      </div>
    )
  }

  const totalElements = pageInfo?.totalElements ?? 0
  const totalPages = pageInfo?.totalPages ?? 0

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie professores e equipe."
        title="Colaboradores"
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar colaborador por nome, função ou email"
            ariaLabel="Buscar colaborador"
          />
          <ButtonLink className="sm:ml-auto" to="/employees/new" variant="success">
            Novo colaborador
          </ButtonLink>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <div className="overflow-x-auto">
          <table className="table table-zebra w-full">
            <thead className="bg-base-200/90">
              <tr>
                <th className="app-th">Nome</th>
                <th className="app-th">Função</th>
                <th className="app-th hidden lg:table-cell">Email</th>
                <th className="app-th hidden lg:table-cell">PIX</th>
                <th className="app-th">Ações</th>
                <th className="app-th-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {employeeList.map((employee) => (
                <tr className="transition-colors hover:bg-base-200/70" key={employee.id}>
                  <td>{employee.name}</td>
                  <td>{dutyLabels[employee.duty]}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">{employee.email}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">{employee.pix}</td>
                  <td>
                    <ButtonLink size="sm" to={`/employees/${employee.id}`} variant="outline">
                      Detalhes
                    </ButtonLink>
                  </td>
                  <td className="text-center">
                    <span className={`badge ${employee.archivedAt ? "badge-warning" : "badge-success"}`}>
                      {employee.archivedAt ? "Arquivado" : "Ativo"}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      {/* PAGINAÇÃO */}
      {totalPages > 1 && (
        <div className="mt-4 flex items-center justify-between px-2">
          <p className="text-sm app-text-muted">
            Mostrando {employeeList.length} de {totalElements} colaboradores
          </p>
          <div className="join">
            <button
              className="btn btn-sm join-item"
              disabled={currentPage === 0}
              onClick={() => setCurrentPage((p) => p - 1)}
            >
              Anterior
            </button>
            <button className="btn btn-sm join-item no-animation cursor-default">
              Página {currentPage + 1} de {totalPages}
            </button>
            <button
              className="btn btn-sm join-item"
              disabled={currentPage >= totalPages - 1}
              onClick={() => setCurrentPage((p) => p + 1)}
            >
              Próxima
            </button>
          </div>
        </div>
      )}

      {employeeList.length === 0 ? (
        <EmptyCard
          title="Nenhum colaborador cadastrado"
          description="Quando você cadastrar o primeiro colaborador, ele aparecerá na tabela acima."
          action={
            <ButtonLink to="/employees/new" variant="secondary">
              Novo colaborador
            </ButtonLink>
          }
        />
      ) : null}
    </div>
  )
}
