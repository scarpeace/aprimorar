import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import styles from "@/features/students/pages/StudentDetailPage.module.css"
import type { StudentResponse } from "@/lib/schemas"
import { getFriendlyErrorMessage, studentsApi } from "@/services/api"
import { CheckCircle, GraduationCap, Mail, MapPin, School, User } from "lucide-react"
import { useEffect, useMemo, useState } from "react"
import { Link, useParams } from "react-router-dom"

function formatDate(value: string) {
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return new Intl.DateTimeFormat("pt-BR").format(date)
}

function DetailField({ label, value, icon: Icon }: { label: string; value: string; icon?: React.ElementType }) {
  return (
    <div className="flex items-start gap-3 rounded-lg border border-gray-200 bg-white p-4 shadow-sm">
      {Icon ? <Icon className="mt-0.5 h-5 w-5 text-gray-400" /> : null}
      <div className="flex-1">
        <p className="text-xs font-medium uppercase tracking-wide text-gray-500">{label}</p>
        <p className="mt-1 text-sm font-semibold text-gray-900">{value}</p>
      </div>
    </div>
  )
}

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [student, setStudent] = useState<StudentResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!id) {
      setError("ID do aluno não informado.")
      setLoading(false)
      return
    }

    const fetchStudent = async () => {
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
    }

    fetchStudent()
  }, [id])

  const addressLabel = useMemo(() => {
    if (!student?.address) {
      return "Endereço não informado"
    }

    const { street, number, complement, district, city, state, zip } = student.address
    return [street, number, complement, district, city, state, zip].filter(Boolean).join(", ")
  }, [student?.address])

  if (loading) return <div>Carregando dados do aluno...</div>
  if (error) return <div>{error}</div>
  if (!student) return <div>Aluno não encontrado.</div>

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
            <DetailField label="Responsável" value={student.parent?.name ?? "Não informado"} icon={User} />
            <DetailField label="Email do responsável" value={student.parent?.email ?? "Não informado"} icon={Mail} />
            <DetailField label="Contato do responsável" value={student.parent?.contact ?? "Não informado"} icon={User} />
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
            <DetailField label="Endereço completo" value={addressLabel} icon={MapPin} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
