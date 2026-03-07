import { EventForm } from "@/features/events/EventForm"
import type { CreateEventInput } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"
import { useState } from "react"
import { useNavigate } from "react-router-dom"

export function EventCreatePage() {
  const navigate = useNavigate()
  const [submitError, setSubmitError] = useState<string | null>(null)

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
      submitError={submitError}
      onSubmit={handleSubmit}
    />
  )
}
