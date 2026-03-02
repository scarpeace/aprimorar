import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import type { EmployeeResponse } from "@/lib/schemas"
import { employeesApi, getFriendlyErrorMessage, type PageResponse } from "@/services/api"
import styles from "@/features/employees/EmployeesPage.module.css"

export function EmployeesPage() {
  const [employeeList, setEmployeeList] = useState<EmployeeResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const loadEmployees = async () => {
    try {
      setError(null)
      setLoading(true)
      const employeesRes = await employeesApi.list()
      const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data
      setEmployeeList(employeesPage.content)
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

  if (loading) {
    return <div>Carregando...</div>
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Colaboradores</h1>
          <p className="text-sm text-gray-600">Gerencie professores e equipe.</p>
        </div>
        <EmptyState
          title="Nao foi possivel carregar"
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
        <Button type="button">Novo colaborador</Button>
      </div>

      <div className={styles.tableWrap}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome</TableHead>
              <TableHead>Cargo</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>PIX</TableHead>
              <TableHead>Ativo</TableHead>
              <TableHead>Acoes</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {employeeList.map((employee) => (
              <TableRow key={employee.id}>
                <TableCell>{employee.name}</TableCell>
                <TableCell>{employee.role}</TableCell>
                <TableCell>{employee.email}</TableCell>
                <TableCell>{employee.pix}</TableCell>
                <TableCell>{employee.active ? "Sim" : "Nao"}</TableCell>
                <TableCell>
                  <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/employees/${employee.id}`}>
                    Detalhes
                  </Link>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {employeeList.length === 0 ? (
        <EmptyState
          title="Nenhum colaborador cadastrado"
          description="Quando voce cadastrar o primeiro colaborador, ele aparecera na tabela acima."
          actionLabel="Novo colaborador"
        />
      ) : null}
    </div>
  )
}
