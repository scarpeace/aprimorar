import { EventForm } from "@/features/events/components/EventForm"
import type { CreateEventInput, EmployeeResponse, StudentResponse } from "@/lib/schemas"
import { eventsApi, employeesApi, getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"

export function EventCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [students, setStudents] = useState<StudentResponse[]>([])
  const [employees, setEmployees] = useState<EmployeeResponse[]>([])
  const [optionsLoading, setOptionsLoading] = useState(true)
  const [optionsError, setOptionsError] = useState<string | null>(null)

  const loadOptions = useCallback(async () => {
    try {
      setOptionsError(null)
      setOptionsLoading(true)

      const [studentsRes, employeesRes] = await Promise.all([
        studentsApi.list(0, 100, "name"),
        employeesApi.listActive(0, 100, "name"),
      ])

      const studentsPage: PageResponse<StudentResponse> = studentsRes.data
      const employeesPage: PageResponse<EmployeeResponse> = employeesRes.data

      setStudents(studentsPage.content)
      setEmployees(employeesPage.content)
    } catch (error) {
      console.error("Falha ao carregar opções:", error)
      setOptionsError(getFriendlyErrorMessage(error))
    } finally {
      setOptionsLoading(false)
    }
  }, [])

  useEffect(() => {
    loadOptions()
  }, [loadOptions])

  const handleSubmit = async (data: CreateEventInput) => {
    try {
      setSubmitError(null)
      const res = await eventsApi.create(data)
      navigate(`/events/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao criar evento:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  return (
    <EventForm
      title="Novo evento"
      description="Crie um novo atendimento ou aula."
      cardDescription="Informe data, valores e participantes do atendimento."
      submitLabel="Criar evento"
      options={{
        students,
        employees,
        loading: optionsLoading,
        error: optionsError,
        onReload: loadOptions,
      }}
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
