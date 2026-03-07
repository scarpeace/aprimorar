import { StudentForm, type StudentParentMode } from "@/features/students/components/StudentForm"
import type { CreateStudentInput, ParentSummary } from "@/lib/schemas"
import { getFriendlyErrorMessage, parentsApi, studentsApi, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"

export function StudentCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [parentMode, setParentMode] = useState<StudentParentMode>("new")
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
      setParentMode(page.content.length > 0 ? "existing" : "new")
    } catch (error) {
      console.error("Falha ao carregar responsáveis:", error)
      setParents([])
      setParentsError(getFriendlyErrorMessage(error))
      setParentMode("new")
    } finally {
      setParentsLoading(false)
    }
  }, [])

  useEffect(() => {
    loadParents()
  }, [loadParents])

  const handleSubmit = async (data: CreateStudentInput, currentParentMode: StudentParentMode) => {
    try {
      setSubmitError(null)

      const payload = currentParentMode === "existing"
        ? { ...data, parent: undefined }
        : { ...data, parentId: undefined }

      const res = await studentsApi.create(payload)

      navigate(`/students/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao criar aluno:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  return (
    <StudentForm
      mode="create"
      title="Novo aluno"
      description="Crie um novo cadastro de aluno."
      cardDescription="Preencha os dados pessoais, endereço e responsável."
      submitLabel="Criar aluno"
      parentMode={parentMode}
      onParentModeChange={setParentMode}
      parents={parents}
      parentsLoading={parentsLoading}
      parentsError={parentsError}
      onReloadParents={loadParents}
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
