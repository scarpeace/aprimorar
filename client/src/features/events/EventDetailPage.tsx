import { Link, useParams } from "react-router-dom"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import { Calendar } from "lucide-react"
import { eventContentLabels, type EventResponse } from "@/lib/schemas/event"
import { useCallback, useEffect, useState } from "react"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"
import styles from "@/features/events/EventDetailPage.module.css"

function SummaryField({ label, value }: { label: string; value: string }) {
  return (
    <div className={styles.summaryItem}>
      <p className={styles.summaryLabel}>{label}</p>
      <p className={styles.summaryValue}>{value}</p>
    </div>
  )
}

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
      const res = await eventsApi.getById(id)
      setEvent(res.data)
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

  const price = Number(event.price)
  const payment = Number(event.payment)
  const profit = Number.isFinite(price) && Number.isFinite(payment) ? price - payment : 0
  const brl = new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" })

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
            <SummaryField label="ID" value={String(event.id)} />
            <SummaryField label="Título" value={event.title} />
            <SummaryField label="Descrição" value={event.description ?? "-"} />
            <SummaryField label="Conteúdo" value={eventContentLabels[event.content]} />
            <SummaryField label="Aluno" value={event.studentName} />
            <SummaryField label="Colaborador" value={event.employeeName} />
            <SummaryField label="Início" value={event.startDateTime} />
            <SummaryField label="Fim" value={event.endDateTime} />
            <SummaryField label="Preço" value={brl.format(price)} />
            <SummaryField label="Pagamento (custo)" value={brl.format(payment)} />
            <SummaryField label="Lucro" value={brl.format(profit)} />
            <SummaryField label="Criado em" value={event.createdAt} />
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
