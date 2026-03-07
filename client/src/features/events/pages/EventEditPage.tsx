import { Button } from "@/components/ui/button"
import { EventForm } from "@/features/events/components/EventForm"
import { mapEventResponseToFormValues } from "@/features/events/utils/eventFormUtils"
import type { CreateEventInput, EventResponse } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"
import { useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"

export function EventEditPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()

  const [initialLoading, setInitialLoading] = useState(true)
  const [initialError, setInitialError] = useState<string | null>(null)
  const [submitError, setSubmitError] = useState<string | null>(null)
  const [initialValues, setInitialValues] = useState<CreateEventInput | undefined>(undefined)

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

        const res = await eventsApi.getById(Number(id))
        const event: EventResponse = res.data
        setInitialValues(mapEventResponseToFormValues(event))
      } catch (error) {
        console.error("Falha ao carregar dados do evento:", error)
        setInitialError(getFriendlyErrorMessage(error))
      } finally {
        setInitialLoading(false)
      }
    }

    loadEvent()
  }, [id])

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
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
