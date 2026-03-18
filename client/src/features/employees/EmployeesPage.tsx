import { useState, useEffect } from "react"
import { UserCog } from "lucide-react"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { Pagination } from "@/components/ui/pagination"
import { ButtonLink } from "@/components/ui/button"
import { dutyLabels } from "@/features/employees/dutyLabels"
import styles from "@/features/employees/EmployeesPage.module.css"
import { getFriendlyErrorMessage } from "@/services/api"
import { useEmployeesQuery } from "./hooks/use-employees"
import { useDebounce } from "@/hooks/use-debounce"

export function EmployeesPage() {
  const [currentPage, setCurrentPage] = useState(0)
  const [searchTerm, setSearchTerm] = useState("")
  const debouncedSearchTerm = useDebounce(searchTerm, 500)
  const pageSize = 10

  const {
    data: employeesPage,
    isLoading,
    isError,
    error,
    refetch,
  } = useEmployeesQuery(currentPage, pageSize, debouncedSearchTerm)

  // Reset pagination when search changes
  useEffect(() => {
    setCurrentPage(0)
  }, [debouncedSearchTerm])

  const employeeList = employeesPage?.content ?? []

  if (isLoading) {
    return <PageLoading message="Carregando colaboradores..." />
  }

  if (isError) {
    return (
      <>
        <PageHeader title="Colaboradores" description="Gerencie professores e equipe." icon={UserCog} />
        <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetch} />
      </>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie professores e equipe."
        title="Colaboradores"
        icon={UserCog}
        action={
          <ButtonLink className="sm:ml-auto" to="/employees/new" variant="success">
            Novo colaborador
          </ButtonLink>
        }
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar colaborador por nome, função ou email"
            ariaLabel="Buscar colaborador"
            value={searchTerm}
            onChange={setSearchTerm}
          />
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <div className="overflow-x-auto">
          <table className="table table-zebra w-full">
            <thead className="bg-base-200/90">
              <tr>
                <th className="app-th">Nome</th>
                <th className="app-th">Função</th>
                <th className="app-th hidden lg:table-cell">PIX</th>
                <th className="app-th hidden lg:table-cell">CPF</th>
                <th className="app-th hidden lg:table-cell">Contato</th>
                <th className="app-th">Ações</th>
                <th className="app-th-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {employeeList.map((employee) => (
                <tr className="transition-colors hover:bg-base-200/70" key={employee.id}>
                  <td>{employee.name}</td>
                  <td>{dutyLabels[employee.duty]}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">{employee.pix}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">{employee.cpf}</td>
                  <td className="hidden whitespace-normal break-all lg:table-cell">{employee.contact}</td>
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

      <Pagination
        currentPage={currentPage}
        totalElements={employeesPage?.page.totalElements ?? 0}
        totalPages={employeesPage?.page.totalPages ?? 0}
        currentElementsCount={employeeList.length}
        itemName="colaboradores"
        onPageChange={setCurrentPage}
      />

      {employeeList.length === 0 && debouncedSearchTerm === "" && (
        <EmptyCard
          title="Nenhum colaborador encontrado"
          description=""
          action={
            <ButtonLink to="/employees/new" variant="secondary">
              Novo aluno
            </ButtonLink>
          }
        />
      )}
    </div>

  )
}
