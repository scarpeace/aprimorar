import { useCallback, useEffect, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { ActionErrorBanner } from "@/components/ui/action-error-banner"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ListPagination } from "@/components/ui/list-pagination"
import { ListPageHeader } from "@/components/ui/list-page-header"
import { PageErrorState } from "@/components/ui/page-error-state"
import { PageLoadingState } from "@/components/ui/page-loading-state"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { EMPLOYEE_ROLE_LABELS } from "@/features/employees/utils/employeeFormUtils"
import type { EmployeeResponse } from "@/lib/schemas"
import { employeesApi, getFriendlyErrorMessage, type PageResponse } from "@/services/api"
import styles from "@/features/employees/pages/EmployeesPage.module.css"

export function EmployeesPage() {
  const navigate = useNavigate()
  const [employeeList, setEmployeeList] = useState<EmployeeResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleteError, setDeleteError] = useState<string | null>(null)
  const [deletingId, setDeletingId] = useState<string | null>(null)
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(10)
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [isFirstPage, setIsFirstPage] = useState(true)
  const [isLastPage, setIsLastPage] = useState(true)

  const loadEmployees = useCallback(async () => {
    try {
      setError(null)
      setLoading(true)

      const employeesRes = await employeesApi.list(page, size)
      const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data

      if (page > 0 && employeesPage.totalPages > 0 && page >= employeesPage.totalPages) {
        setPage(employeesPage.totalPages - 1)
        return
      }

      setEmployeeList(employeesPage.content)
      setTotalElements(employeesPage.totalElements)
      setTotalPages(employeesPage.totalPages)
      setIsFirstPage(employeesPage.first)
      setIsLastPage(employeesPage.last)
    } catch (error) {
      console.error("Falha ao carregar colaboradores:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }, [page, size])

  useEffect(() => {
    loadEmployees()
  }, [loadEmployees])

  const handleDelete = async (employee: EmployeeResponse) => {
    if (!window.confirm(`Excluir colaborador "${employee.name}"? Essa ação não pode ser desfeita.`)) {
      return
    }

    try {
      setDeleteError(null)
      setDeletingId(employee.id)
      await employeesApi.delete(employee.id)
      await loadEmployees()
    } catch (error) {
      console.error("Falha ao excluir colaborador:", error)
      setDeleteError(getFriendlyErrorMessage(error))
    } finally {
      setDeletingId(null)
    }
  }

  if (loading) {
    return <PageLoadingState label="Carregando colaboradores..." />
  }

  if (error) {
    return (
      <PageErrorState
        title="Colaboradores"
        description="Gerencie professores e equipe."
        errorMessage={error}
        onRetry={loadEmployees}
      />
    )
  }

  return (
    <div className={styles.page}>
      <ListPageHeader title="Colaboradores" description="Gerencie professores e equipe." actionLabel="Novo colaborador" actionTo="/employees/new" />

      <ActionErrorBanner message={deleteError} />

      <div className={styles.tableWrap}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Cargo</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>PIX</TableHead>
              <TableHead>Status</TableHead>
              <TableHead>Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {employeeList.map((employee) => (
              <TableRow key={employee.id}>
                <TableCell>{employee.name}</TableCell>
                <TableCell>{EMPLOYEE_ROLE_LABELS[employee.role]}</TableCell>
                <TableCell>{employee.email}</TableCell>
                <TableCell>{employee.pix}</TableCell>
                <TableCell>{employee.active ? "Ativo" : "Inativo"}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/employees/${employee.id}`}>
                      Detalhes
                    </Link>
                    <Button
                      type="button"
                      variant="destructive"
                      size="sm"
                      onClick={() => handleDelete(employee)}
                      disabled={deletingId === employee.id}
                    >
                      {deletingId === employee.id ? "Excluindo..." : "Excluir"}
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <ListPagination
        page={page}
        size={size}
        totalPages={totalPages}
        totalElements={totalElements}
        isFirstPage={isFirstPage}
        isLastPage={isLastPage}
        summaryLabel={`${totalElements} colaborador(es)`}
        selectId="employees-page-size"
        onPageChange={setPage}
        onSizeChange={(nextSize) => {
          setSize(nextSize)
          setPage(0)
        }}
      />

      {employeeList.length === 0 ? (
        <EmptyState
          title="Nenhum colaborador cadastrado"
          description="Quando você cadastrar o primeiro colaborador, ele aparecerá na tabela acima."
          actionLabel="Novo colaborador"
          onAction={() => navigate("/employees/new")}
        />
      ) : null}
    </div>
  )
}
