import { Button } from "@/components/ui/button"
import { StudentForm } from "@/features/students/components/StudentForm"
import { buildStudentPayload, mapStudentResponseToFormValues } from "@/features/students/utils/studentFormUtils"
import type { CreateStudentInput, StudentResponse } from "@/lib/schemas"
import { getFriendlyErrorMessage, studentsApi } from "@/services/api"
import { useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"

export function StudentEditPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()

  const [initialLoading, setInitialLoading] = useState(true)
  const [initialError, setInitialError] = useState<string | null>(null)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [initialValues, setInitialValues] = useState<CreateStudentInput | undefined>(undefined)
  const [initialParentMode, setInitialParentMode] = useState<"existing" | "new">("new")

  useEffect(() => {
    if (!id) {
      setInitialError("ID do aluno não informado.")
      setInitialLoading(false)
      return
    }

    const loadStudent = async () => {
      try {
        setInitialError(null)
        setInitialLoading(true)

        const res = await studentsApi.getById(id)
        const student: StudentResponse = res.data

        setInitialValues(mapStudentResponseToFormValues(student))
        setInitialParentMode(student.parent ? "new" : "existing")
      } catch (error) {
        console.error("Falha ao carregar dados do aluno:", error)
        setInitialError(getFriendlyErrorMessage(error))
      } finally {
        setInitialLoading(false)
      }
    }

    loadStudent()
  }, [id])

  const handleSubmit = async (data: CreateStudentInput, parentMode: "existing" | "new") => {
    if (!id) {
      setSubmitError("ID do aluno não informado.")
      return
    }

    try {
      setSubmitError(null)

      const payload = buildStudentPayload(data, parentMode)
      const res = await studentsApi.update(id, payload)

      navigate(`/students/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao atualizar aluno:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  if (initialLoading) {
    return <div>Carregando dados do aluno...</div>
  }

  if (initialError) {
    return (
      <div className="flex flex-col gap-6">
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Editar aluno</h1>
            <p className="text-sm text-gray-600">Não foi possível carregar os dados para edição.</p>
          </div>
          <Button asChild type="button" variant="outline">
            <Link to="/students">← Voltar para alunos</Link>
          </Button>
        </div>
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">{initialError}</div>
      </div>
    )
  }

  return (
    <StudentForm
      title="Editar aluno"
      description="Atualize os dados do cadastro do aluno."
      cardDescription="Atualize os dados pessoais, endereço e responsável."
      submitLabel="Salvar alterações"
      initialValues={initialValues}
      initialParentMode={initialParentMode}
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
