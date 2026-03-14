import { useQuery } from "@tanstack/react-query"
import { useState } from "react"
import { Link } from "react-router-dom"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { dutyLabels } from "@/features/employees/dutyLabels"
import styles from "@/features/employees/EmployeesPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { employeesApi, getFriendlyErrorMessage } from "@/services/api"

const EMPLOYEES_LIST_PARAMS = { page: 0, size: 20, sortBy: "name" }

export function EmployeesPage() {
  const [hideArchived, setHideArchived] = useState(false)

  const {
    data: employeeList = [],
    isLoading,
    isError,
    error,
    refetch,
  } = useQuery({
    queryKey: queryKeys.employees.list(EMPLOYEES_LIST_PARAMS),
    queryFn: async () => {
      const employeesRes = await employeesApi.list(
        EMPLOYEES_LIST_PARAMS.page,
        EMPLOYEES_LIST_PARAMS.size,
        EMPLOYEES_LIST_PARAMS.sortBy
      )

      return employeesRes.content
    },
  })

  const visibleEmployees = hideArchived ? employeeList.filter((employee) => !employee.archivedAt) : employeeList

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

  return (
    <div className={styles.page}>
      <PageHeader
        action={
          <Link className="btn btn-success" to="/employees/new">
            Novo colaborador
          </Link>
        }
        description="Gerencie professores e equipe."
        title="Colaboradores"
      >
        <div className="flex w-full flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <ListSearchInput
            placeholder="Buscar colaborador por nome, função ou email"
            ariaLabel="Buscar colaborador"
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
                <th className="app-th">Função</th>
                <th className="app-th">Email</th>
                <th className="app-th">PIX</th>
                <th className="app-th">Ações</th>
                <th className="app-th">Status</th>
              </tr>
            </thead>
            <tbody>
              {visibleEmployees.map((employee) => (
                <tr key={employee.id}>
                  <td>{employee.name}</td>
                  <td>{dutyLabels[employee.duty]}</td>
                  <td>{employee.email}</td>
                  <td>{employee.pix}</td>
                  <td>
                    <Link className="btn btn-ghost btn-sm" to={`/employees/${employee.id}`}>
                      Detalhes
                    </Link>
                  </td>
                  <td>
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

      {visibleEmployees.length === 0 ? (
        <EmptyCard
          title="Nenhum colaborador cadastrado"
          description="Quando você cadastrar o primeiro colaborador, ele aparecerá na tabela acima."
          action={
            <Link className="btn btn-secondary" to="/employees/new">
              Novo colaborador
            </Link>
          }
        />
      ) : null}
    </div>
  )
}
