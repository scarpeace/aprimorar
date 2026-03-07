import { Link, useNavigate, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { DetailField } from "@/components/ui/detail-field"
import { EmptyState } from "@/components/ui/empty-state"
import { PageErrorState } from "@/components/ui/page-error-state"
import { PageLoadingState } from "@/components/ui/page-loading-state"
import { EMPLOYEE_ROLE_LABELS } from "@/features/employees/utils/employeeFormUtils"
import { UserCog, Mail, CreditCard, Shield, CheckCircle } from "lucide-react"
import { useCallback, useEffect, useState } from "react"
import type { EmployeeResponse } from "@/lib/schemas"
import { employeesApi, getFriendlyErrorMessage } from "@/services/api"
import styles from "@/features/employees/pages/EmployeeDetailPage.module.css"

export function EmployeeDetailPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const [employee, setEmployee] = useState<EmployeeResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadEmployee = useCallback(async () => {
    if (!id) {
      setError("ID do colaborador não informado.")
      setLoading(false)
      return
    }

    try {
      setLoading(true)
      setError(null)

      const res = await employeesApi.getById(id)
      setEmployee(res.data)
    } catch (error) {
      console.error("Falha ao carregar colaborador:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    loadEmployee()
  }, [loadEmployee])

  if (loading) return <PageLoadingState label="Carregando colaborador..." />
  if (error) {
    return (
      <PageErrorState
        title="Detalhes do colaborador"
        description="Veja e gerencie as informações do colaborador."
        errorMessage={error}
        onRetry={loadEmployee}
      />
    )
  }
  if (!employee) {
    return (
      <div className={styles.page}>
        <EmptyState
          title="Colaborador não encontrado"
          description="Não encontramos um cadastro de colaborador com os dados informados."
          actionLabel="Voltar para colaboradores"
          onAction={() => navigate("/employees")}
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-green-100">
            <UserCog className="h-6 w-6 text-green-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Detalhes do colaborador</h1>
            <p className="text-sm text-gray-500">Veja e gerencie as informações do colaborador.</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/employees">← Voltar para colaboradores</Link>
        </Button>
      </div>

      <div className={styles.contentGrid}>
        <Card className="border-l-4 border-l-green-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <UserCog className="h-5 w-5 text-green-500" />
              Informações pessoais
            </CardTitle>
            <CardDescription>Dados principais do colaborador.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Nome completo" value={employee.name} icon={UserCog} />
            <DetailField label="Email" value={employee.email} icon={Mail} />
            <DetailField label="Cargo" value={EMPLOYEE_ROLE_LABELS[employee.role]} icon={Shield} />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-orange-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <CreditCard className="h-5 w-5 text-orange-500" />
              Pagamento e status
            </CardTitle>
            <CardDescription>PIX e status da conta.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Chave PIX" value={employee.pix} icon={CreditCard} />
            <DetailField label="Status" value={employee.active ? "Ativo" : "Inativo"} icon={CheckCircle} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
