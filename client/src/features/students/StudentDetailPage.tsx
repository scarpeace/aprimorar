import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { GraduationCap, Mail, School, MapPin, User, CheckCircle } from "lucide-react"
import { getFriendlyErrorMessage, studentsApi } from "@/services/api"
import { useEffect, useState } from "react"
import type { StudentResponse } from "@/lib/schemas"
import styles from "@/features/students/StudentDetailPage.module.css"

//TODO Improve layout on this page/component + Translate labels + Errors to portuguese
function DetailField({ label, value, icon: Icon }: { label: string; value: string; icon?: React.ElementType }) {
  return (
    <div className="flex items-start gap-3 rounded-lg border border-gray-200 bg-white p-4 shadow-sm">
      {Icon && <Icon className="mt-0.5 h-5 w-5 text-gray-400" />}
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
      return;
    }

     const fetchStudent = async () => {
       try {
         setLoading(true)
         setError(null)

         const res = await studentsApi.getById(id)
         setStudent(res.data)
       } catch (error) {
         console.error("Falha ao carregar aluno:", error)
         setError(getFriendlyErrorMessage(error))
         } finally {
           setLoading(false)
         }
     }
       fetchStudent();
     }, [id])

     if (loading) return <div>Carregando...</div>
     if(error) return <div>{error}</div>
     if(!student) return <div>Aluno não encontrado.</div>

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-blue-100">
            <GraduationCap className="h-6 w-6 text-blue-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Detalhes do aluno</h1>
            <p className="text-sm text-gray-500">Veja e gerencie as informações do aluno</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/students">
            ← Voltar para alunos
          </Link>
        </Button>
      </div>

      <div className={styles.contentGrid}>
        <Card className="border-l-4 border-l-blue-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <User className="h-5 w-5 text-blue-500" />
              Informações pessoais
            </CardTitle>
            <CardDescription>Dados principais do aluno</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Nome Completo" value={student.name} icon={User} />
            <DetailField label="CPF" value={student.cpf} icon={Mail} />
            <DetailField label="Email" value={student.email} icon={GraduationCap} />
            <DetailField label="Idade" value={String(student.age)} icon={GraduationCap} />
            <DetailField label="Contato" value={student.contact} icon={GraduationCap} />
            <DetailField label="Data Nascimento" value={student.birthdate} icon={GraduationCap} />
            <DetailField label="Matrícula" value={student.createdAt} icon={GraduationCap} />
            <DetailField label="Escola" value={student.school} icon={GraduationCap} />
            <DetailField label="Responsável" value={student.parent?.name ?? "-"} icon={GraduationCap} />
            <DetailField
              label="Status"
              value={student.archivedAt ? "Arquivado" : "Ativo"}
              icon={CheckCircle}
            />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-purple-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <School className="h-5 w-5 text-purple-500" />
              Academic & Status
            </CardTitle>
            <CardDescription>School and enrollment details</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="School" value="-" icon={School} />
            <DetailField label="Address" value="-" icon={MapPin} />
            <DetailField label="Status" value="-" icon={CheckCircle} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
