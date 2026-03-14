import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import { SummaryField } from "@/components/ui/summary-field"
import { Calendar } from "lucide-react"
import type { EventResponse } from "@/lib/schemas/event"
import { eventContentLabels } from "@/lib/shared/enums"
import { brl, formatDateShortYear, formatTime } from "@/lib/shared/formatter"
import { useCallback, useEffect, useState } from "react"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"
import styles from "@/features/events/EventDetailPage.module.css"

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>()
  const [event, setEvent] = useState<EventResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadEvent = useCallback(async () => {
    if (!id) {
      setError("ID do evento não informado.")
      return
    }

    try {
      setLoading(true)
      setError(null)
      const res: EventResponse = await eventsApi.getById(id)
      setEvent(res)
    } catch (loadError) {
      console.error("Falha ao carregar evento:", loadError)
      setError(getFriendlyErrorMessage(loadError))
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    loadEvent()
  }, [loadEvent])

  const profit = Number(event?.price ?? 0) - Number(event?.payment ?? 0)


  if (loading) return <LoadingState message="Carregando evento..." />

  if (error) {
    return (
      <div className={styles.page}>
        <ErrorState
          title="Não foi possível carregar"
          description={error}
          actionLabel="Tentar novamente"
          onAction={loadEvent}
        />
      </div>
    )
  }

  if (!event) {
    return (
      <div className={styles.page}>
        <EmptyState title="Evento não encontrado" description="Não encontramos os dados deste evento." />
      </div>
    )
  }


  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-purple-100">
            <Calendar className="h-6 w-6 text-purple-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Detalhes do evento</h1>
            <p className="text-sm text-gray-500">Veja e gerencie as informações do evento</p>
          </div>
        </div>
        <Button asChild type="button" variant="outline">
          <Link to="/events">← Voltar para eventos</Link>
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Resumo do evento</CardTitle>
          <CardDescription>Dados completos do atendimento, participantes e valores.</CardDescription>
        </CardHeader>
        <CardContent>
          <div className={styles.summaryGrid}>
            <SummaryField title="ID">{String(event.id)}</SummaryField>
            <SummaryField title="Título">{event.title}</SummaryField>
            <SummaryField title="Descrição">{event.description ?? "-"}</SummaryField>
            <SummaryField title="Conteúdo">{eventContentLabels[event.content]}</SummaryField>
            <SummaryField title="Aluno">{event.studentName}</SummaryField>
            <SummaryField title="Colaborador">{event.employeeName}</SummaryField>
            <SummaryField title="Data">{formatDateShortYear(event.startDate)}</SummaryField>
            <SummaryField title="Horário">{`${formatTime(event.startDate)} : ${formatTime(event.endDate)}`}</SummaryField>
            <SummaryField title="Preço">{brl.format(event.price)}</SummaryField>
            <SummaryField title="Pagamento (custo)">{brl.format(event.payment)}</SummaryField>
            <SummaryField title="Lucro">{brl.format(profit)}</SummaryField>
            <SummaryField title="Criado em">{formatDateShortYear(event.createdAt)}</SummaryField>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
