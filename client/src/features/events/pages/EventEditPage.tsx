import { Button } from "@/components/ui/button"
import { EventForm } from "@/features/events/components/EventForm"
import { mapEventResponseToFormValues } from "@/features/events/utils/eventFormUtils"
import type { CreateEventInput, EmployeeResponse, EventResponse, StudentResponse } from "@/lib/schemas"
import { eventsApi, employeesApi, getFriendlyErrorMessage, studentsApi, type PageResponse } from "@/services/api"
import { useCallback, useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"

export function EventEditPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const [students, setStudents] = useState<StudentResponse[]>([])
  const [employees, setEmployees] = useState<EmployeeResponse[]>([])
  const [optionsLoading, setOptionsLoading] = useState(true)
  const [optionsError, setOptionsError] = useState<string | null>(null)

  const [initialLoading, setInitialLoading] = useState(true)
  const [initialError, setInitialError] = useState<string | null>(null)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [initialValues, setInitialValues] = useState<CreateEventInput | undefined>(undefined)

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
    if (!id) {
      setInitialError("ID do evento não informado.")
      setInitialLoading(false)
      return
    }

    const loadEvent = async () => {
      try {
        setInitialError(null)
        setInitialLoading(true)

        const [eventRes] = await Promise.all([
          eventsApi.getById(Number(id)),
          loadOptions(),
        ])

        const event: EventResponse = eventRes.data
        setInitialValues(mapEventResponseToFormValues(event))
      } catch (error) {
        console.error("Falha ao carregar dados do evento:", error)
        setInitialError(getFriendlyErrorMessage(error))
      } finally {
        setInitialLoading(false)
      }
    }

    loadEvent()
  }, [id, loadOptions])

  const handleSubmit = async (data: CreateEventInput) => {
    if (!id) {
      setSubmitError("ID do evento não informado.")
      return
    }

    try {
      setSubmitError(null)
      const res = await eventsApi.update(Number(id), data)
      navigate(`/events/${res.data.id}`)
    } catch (error) {
      console.error("Falha ao atualizar evento:", error)
      setSubmitError(getFriendlyErrorMessage(error))
    }
  }

  if (initialLoading) {
    return <div>Carregando dados do evento...</div>
  }

  if (initialError) {
    return (
      <div className="flex flex-col gap-6">
        <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Editar evento</h1>
            <p className="text-sm text-gray-600">Não foi possível carregar os dados para edição.</p>
          </div>
          <Button asChild type="button" variant="outline">
            <Link to="/events">← Voltar para eventos</Link>
          </Button>
        </div>
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">{initialError}</div>
      </div>
    )
  }

  return (
    <EventForm
      title="Editar evento"
      description="Atualize os dados do atendimento ou aula."
      cardDescription="Revise data, valores e participantes do evento."
      submitLabel="Salvar alterações"
      initialValues={initialValues}
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
