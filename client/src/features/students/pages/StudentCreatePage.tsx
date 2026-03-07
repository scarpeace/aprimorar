import { StudentForm } from "@/features/students/components/StudentForm"
import { buildStudentPayload } from "@/features/students/utils/studentFormUtils"
import type { CreateStudentInput } from "@/lib/schemas"
import { getFriendlyErrorMessage, studentsApi } from "@/services/api"
import { useState } from "react"
import { useNavigate } from "react-router-dom"

export function StudentCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)

  const handleSubmit = async (data: CreateStudentInput, parentMode: "existing" | "new") => {
    try {
      setSubmitError(null)

      const payload = buildStudentPayload(data, parentMode)
      const res = await studentsApi.create(payload)

      navigate(`/students/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao criar aluno:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  return (
    <StudentForm
      title="Novo aluno"
      description="Crie um novo cadastro de aluno."
      cardDescription="Preencha os dados pessoais, endereço e responsável."
      submitLabel="Criar aluno"
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
