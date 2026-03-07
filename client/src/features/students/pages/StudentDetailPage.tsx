import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { DetailField } from "@/components/ui/detail-field"
import { EmptyState } from "@/components/ui/empty-state"
import { PageErrorState } from "@/components/ui/page-error-state"
import { PageLoadingState } from "@/components/ui/page-loading-state"
import styles from "@/features/students/pages/StudentDetailPage.module.css"
import type { StudentResponse } from "@/lib/schemas"
import { getFriendlyErrorMessage, studentsApi } from "@/services/api"
import { CheckCircle, GraduationCap, Hash, Mail, MapPin, MapPinned, School, User } from "lucide-react"
import { useCallback, useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"

function formatDate(value: string) {
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return new Intl.DateTimeFormat("pt-BR").format(date)
}

function formatOptionalValue(value: string | null | undefined) {
  return value?.trim() ? value : "Não informado"
}

export function StudentDetailPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const [student, setStudent] = useState<StudentResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadStudent = useCallback(async () => {
    if (!id) {
      setError("ID do aluno não informado.")
      setLoading(false)
      return
    }

    try {
      setLoading(true)
      setError(null)

      const res = await studentsApi.getById(id)
      setStudent(res.data)
    } catch (requestError) {
      console.error("Falha ao carregar aluno:", requestError)
      setError(getFriendlyErrorMessage(requestError))
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    loadStudent()
  }, [loadStudent])

  if (loading) return <PageLoadingState label="Carregando dados do aluno..." />
  if (error) {
    return (
      <PageErrorState
        title="Detalhes do aluno"
        description="Veja e gerencie as informações do cadastro do aluno."
        errorMessage={error}
        onRetry={loadStudent}
      />
    )
  }
  if (!student) {
    return (
      <div className={styles.page}>
        <EmptyState
          title="Aluno não encontrado"
          description="Não encontramos um cadastro de aluno com os dados informados."
          actionLabel="Voltar para alunos"
          onAction={() => navigate("/students")}
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-blue-100">
            <GraduationCap className="h-6 w-6 text-blue-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Detalhes do aluno</h1>
            <p className="text-sm text-gray-500">Veja e gerencie as informações do cadastro do aluno.</p>
          </div>
        </div>
        <div className={styles.headerActions}>
          <Button asChild type="button" variant="outline">
            <Link to="/students">← Voltar para alunos</Link>
          </Button>
          <Button asChild type="button">
            <Link to={`/students/${student.id}/edit`}>Editar aluno</Link>
          </Button>
        </div>
      </div>

      <div className={styles.contentGrid}>
        <Card className="border-l-4 border-l-blue-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <User className="h-5 w-5 text-blue-500" />
              Informações pessoais
            </CardTitle>
            <CardDescription>Dados principais do aluno.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Nome completo" value={student.name} icon={User} />
            <DetailField label="CPF" value={student.cpf} icon={Mail} />
            <DetailField label="Email" value={student.email} icon={Mail} />
            <DetailField label="Contato" value={student.contact} icon={User} />
            <DetailField label="Data de nascimento" value={formatDate(student.birthdate)} icon={GraduationCap} />
            <DetailField label="Idade" value={`${student.age} anos`} icon={GraduationCap} />
            <DetailField label="Matrícula" value={formatDate(student.createdAt)} icon={CheckCircle} />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-emerald-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <School className="h-5 w-5 text-emerald-500" />
              Escola e responsável
            </CardTitle>
            <CardDescription>Dados acadêmicos e vínculo familiar.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Escola" value={student.school || "Não informada"} icon={School} />
            <DetailField label="Responsável" value={formatOptionalValue(student.parent?.name)} icon={User} />
            <DetailField label="Email do responsável" value={formatOptionalValue(student.parent?.email)} icon={Mail} />
            <DetailField label="Contato do responsável" value={formatOptionalValue(student.parent?.contact)} icon={User} />
            <DetailField label="Status" value={student.archivedAt ? "Arquivado" : "Ativo"} icon={CheckCircle} />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-amber-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <MapPin className="h-5 w-5 text-amber-500" />
              Endereço
            </CardTitle>
            <CardDescription>Endereço residencial cadastrado.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Rua" value={formatOptionalValue(student.address?.street)} icon={MapPin} />
            <DetailField label="Número" value={formatOptionalValue(student.address?.number)} icon={Hash} />
            <DetailField label="Complemento" value={formatOptionalValue(student.address?.complement)} icon={MapPinned} />
            <DetailField label="Bairro" value={formatOptionalValue(student.address?.district)} icon={MapPin} />
            <DetailField label="Cidade" value={formatOptionalValue(student.address?.city)} icon={MapPin} />
            <DetailField label="Estado" value={formatOptionalValue(student.address?.state)} icon={MapPin} />
            <DetailField label="CEP" value={formatOptionalValue(student.address?.zip)} icon={MapPinned} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
