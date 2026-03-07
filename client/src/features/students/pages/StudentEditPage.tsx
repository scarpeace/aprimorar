import { Button } from "@/components/ui/button"
import { StudentForm, type StudentParentMode } from "@/features/students/components/StudentForm"
import { mapStudentResponseToFormValues } from "@/features/students/utils/studentFormUtils"
import type { CreateStudentInput, ParentSummary, StudentResponse } from "@/lib/schemas"
import { getFriendlyErrorMessage, parentsApi, studentsApi, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"

export function StudentEditPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()

  const [initialLoading, setInitialLoading] = useState(true)
  const [initialError, setInitialError] = useState<string | null>(null)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [initialValues, setInitialValues] = useState<CreateStudentInput | undefined>(undefined)
  const [parentMode, setParentMode] = useState<StudentParentMode>("editCurrent")
  const [parents, setParents] = useState<ParentSummary[]>([])
  const [parentsLoading, setParentsLoading] = useState(true)
  const [parentsError, setParentsError] = useState<string | null>(null)

  const loadParents = useCallback(async () => {
    try {
      setParentsLoading(true)
      setParentsError(null)

      const res = await parentsApi.listActive(0, 100, "name")
      const page: PageResponse<ParentSummary> = res.data

      setParents(page.content)
    } catch (error) {
      console.error("Falha ao carregar responsáveis:", error)
      setParents([])
      setParentsError(getFriendlyErrorMessage(error))
    } finally {
      setParentsLoading(false)
    }
  }, [])

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

        const [studentRes] = await Promise.all([
          studentsApi.getById(id),
          loadParents(),
        ])

        const student: StudentResponse = studentRes.data

        setInitialValues(mapStudentResponseToFormValues(student))
        setParentMode("editCurrent")
      } catch (error) {
        console.error("Falha ao carregar dados do aluno:", error)
        setInitialError(getFriendlyErrorMessage(error))
      } finally {
        setInitialLoading(false)
      }
    }

    loadStudent()
  }, [id, loadParents])

  const handleSubmit = async (data: CreateStudentInput, currentParentMode: StudentParentMode) => {
    if (!id) {
      setSubmitError("ID do aluno não informado.")
      return
    }

    try {
      setSubmitError(null)

      const payload = currentParentMode === "switchExisting"
        ? { ...data, parent: undefined }
        : { ...data, parentId: undefined }

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
      mode="edit"
      title="Editar aluno"
      description="Atualize os dados do cadastro do aluno."
      cardDescription="Atualize os dados pessoais, endereço e responsável."
      submitLabel="Salvar alterações"
      initialValues={initialValues}
      parentMode={parentMode}
      onParentModeChange={setParentMode}
      parents={parents}
      parentsLoading={parentsLoading}
      parentsError={parentsError}
      onReloadParents={loadParents}
      parentWarning="As alterações neste responsável também serão refletidas em outros alunos vinculados a ele."
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
