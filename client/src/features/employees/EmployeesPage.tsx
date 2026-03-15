import { useQuery } from "@tanstack/react-query"
import { useState } from "react"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { ButtonLink } from "@/components/ui/button"
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
    queryKey: [...queryKeys.employees, EMPLOYEES_LIST_PARAMS],
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
        description="Gerencie professores e equipe."
        title="Colaboradores"
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar colaborador por nome, função ou email"
            ariaLabel="Buscar colaborador"
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
              {visibleEmployees.map((employee) => (
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

      {visibleEmployees.length === 0 ? (
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
