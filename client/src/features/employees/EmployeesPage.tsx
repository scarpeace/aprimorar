import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { dutyLabels } from "@/features/employees/dutyLabels"
import type { EmployeeResponse } from "@/lib/schemas"
import { employeesApi, getFriendlyErrorMessage, } from "@/services/api"
import styles from "@/features/employees/EmployeesPage.module.css"
import type { PageResponse } from "@/lib/schemas/page-response"

export function EmployeesPage() {
  const [employeeList, setEmployeeList] = useState<EmployeeResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [archiveError, setArchivedError] = useState<string | null>(null)
  const [archivingId, setarchivingId] = useState<string | null>(null)

  const loadEmployees = async () => {
    try {
      setError(null)
      setLoading(true)
      const employeesRes : PageResponse<EmployeeResponse> = await employeesApi.list()
      setEmployeeList(employeesRes.content)
    } catch (error) {
      console.error("Falha ao carregar colaboradores:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadEmployees()
  }, [])

  const handleArchive = async (employee: EmployeeResponse) => {
    if (!window.confirm(`Arquivar colaborador "${employee.name}"?`)) {
      return
    }

    try {
      setArchivedError(null)
      setarchivingId(employee.id)
      await employeesApi.archive(employee.id)
      setEmployeeList((prev) => prev.filter((item) => item.id !== employee.id))
    } catch (error) {
      console.error("Falha ao excluir colaborador:", error)
      setArchivedError(getFriendlyErrorMessage(error))
    } finally {
      setarchivingId(null)
    }
  }

  if (loading) {
    return <LoadingState message="Carregando colaboradores..." />
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Colaboradores</h1>
          <p className="text-sm text-gray-600">Gerencie professores e equipe.</p>
        </div>
        <ErrorState
          title="Não foi possível carregar"
          description={error}
          actionLabel="Tentar novamente"
          onAction={loadEmployees}
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Colaboradores</h1>
          <p className="text-sm text-gray-600">Gerencie professores e equipe.</p>
        </div>
        <Button asChild type="button">
          <Link to="/employees/new">Novo colaborador</Link>
        </Button>
      </div>

      {archiveError ? (
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
          {archiveError}
        </div>
      ) : null}

      <div className={styles.tableWrap}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Cargo</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>PIX</TableHead>
              <TableHead>Ativo</TableHead>
              <TableHead>Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {employeeList.map((employee) => (
              <TableRow key={employee.id}>
                <TableCell>{employee.name}</TableCell>
                <TableCell>{dutyLabels[employee.duty]}</TableCell>
                <TableCell>{employee.email}</TableCell>
                <TableCell>{employee.pix}</TableCell>
                <TableCell>{employee.archivedAt ? "Não" : "Sim"}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <Link
                      className="text-sm font-medium text-blue-600 hover:underline"
                      to={`/employees/${employee.id}`}
                    >
                      Detalhes
                    </Link>
                    <Button
                      type="button"
                      variant="destructive"
                      size="sm"
                      onClick={() => handleArchive(employee)}
                      disabled={archivingId === employee.id}
                    >
                      {archivingId === employee.id ? "Arquivando..." : "Arquivar"}
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {employeeList.length === 0 ? (
        <EmptyState
          title="Nenhum colaborador cadastrado"
          description="Quando você cadastrar o primeiro colaborador, ele aparecerá na tabela acima."
          actionLabel="Novo colaborador"
        />
      ) : null}
    </div>
  )
}
