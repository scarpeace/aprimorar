import { EmployeeForm } from "@/features/employees/components/EmployeeForm"
import type { CreateEmployeeInput } from "@/lib/schemas"
import { employeesApi, getFriendlyErrorMessage } from "@/services/api"
import { useState } from "react"
import { useNavigate } from "react-router-dom"

export function EmployeeCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)

  const handleSubmit = async (data: CreateEmployeeInput) => {
    try {
      setSubmitError(null)
      const res = await employeesApi.create(data)
      navigate(`/employees/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao criar colaborador:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  return (
    <EmployeeForm
      title="Novo colaborador"
      description="Crie um novo cadastro de colaborador."
      cardDescription="Preencha as informações abaixo para criar o cadastro."
      submitLabel="Criar colaborador"
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
