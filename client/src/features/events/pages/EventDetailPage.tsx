import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { DetailField } from "@/components/ui/detail-field"
import { EmptyState } from "@/components/ui/empty-state"
import { PageErrorState } from "@/components/ui/page-error-state"
import { PageLoadingState } from "@/components/ui/page-loading-state"
import styles from "@/features/events/pages/EventDetailPage.module.css"
import { eventContentLabels, type EventResponse } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"
import { Calendar, Clock, DollarSign, GraduationCap, User } from "lucide-react"
import { useCallback, useMemo, useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"

function formatDateTime(value: string) {
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) {
    return value
  }

  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short",
  }).format(date)
}

export function EventDetailPage() {
  const navigate = useNavigate()
  const { id } = useParams<{ id: string }>()
  const [event, setEvent] = useState<EventResponse | null>(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadEvent = useCallback(async () => {
    if (!id) {
      setError("ID do evento não informado.")
      setLoading(false)
      return
    }

    try {
      setLoading(true)
      setError(null)

      const res = await eventsApi.getById(Number(id))
      setEvent(res.data)
    } catch (requestError) {
      console.error("Falha ao carregar evento:", requestError)
      setError(getFriendlyErrorMessage(requestError))
    } finally {
      setLoading(false)
    }
  }, [id])

  useEffect(() => {
    loadEvent()
  }, [loadEvent])

  const currencyFormatter = useMemo(
    () =>
      new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL",
      }),
    []
  )

  if (loading) return <PageLoadingState label="Carregando dados do evento..." />
  if (error) {
    return (
      <PageErrorState
        title="Detalhes do evento"
        description="Veja e gerencie as informações do atendimento."
        errorMessage={error}
        onRetry={loadEvent}
      />
    )
  }
  if (!event) {
    return (
      <div className={styles.page}>
        <EmptyState
          title="Evento não encontrado"
          description="Não encontramos um evento com os dados informados."
          actionLabel="Voltar para eventos"
          onAction={() => navigate("/events")}
        />
      </div>
    )
  }

  const price = Number(event.price)
  const payment = Number(event.payment)
  const profit = Number.isFinite(price) && Number.isFinite(payment) ? price - payment : 0

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-amber-100">
            <Calendar className="h-6 w-6 text-amber-600" />
          </div>
          <div>
            <h1 className="text-2xl font-bold text-gray-900">Detalhes do evento</h1>
            <p className="text-sm text-gray-500">Veja e gerencie as informações do atendimento.</p>
          </div>
        </div>
        <div className={styles.headerActions}>
          <Button asChild type="button" variant="outline">
            <Link to="/events">← Voltar para eventos</Link>
          </Button>
          <Button asChild type="button">
            <Link to={`/events/${event.id}/edit`}>Editar evento</Link>
          </Button>
        </div>
      </div>

      <div className={styles.contentGrid}>
        <Card className="border-l-4 border-l-amber-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <Calendar className="h-5 w-5 text-amber-500" />
              Dados do atendimento
            </CardTitle>
            <CardDescription>Resumo principal do evento.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Título" value={event.title} icon={Calendar} />
            <DetailField label="Descrição" value={event.description ?? "Sem descrição"} icon={Calendar} />
            <DetailField label="Conteúdo" value={eventContentLabels[event.content]} icon={Calendar} />
            <DetailField label="Início" value={formatDateTime(event.startDateTime)} icon={Clock} />
            <DetailField label="Fim" value={formatDateTime(event.endDateTime)} icon={Clock} />
          </CardContent>
        </Card>

        <Card className="border-l-4 border-l-indigo-500">
          <CardHeader className="pb-3">
            <CardTitle className="flex items-center gap-2 text-lg">
              <User className="h-5 w-5 text-indigo-500" />
              Participantes e valores
            </CardTitle>
            <CardDescription>Aluno, colaborador e resumo financeiro.</CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <DetailField label="Aluno" value={event.studentName} icon={GraduationCap} />
            <DetailField label="Colaborador" value={event.employeeName} icon={User} />
            <DetailField label="Preço" value={currencyFormatter.format(price)} icon={DollarSign} />
            <DetailField label="Pagamento" value={currencyFormatter.format(payment)} icon={DollarSign} />
            <DetailField label="Lucro" value={currencyFormatter.format(profit)} icon={DollarSign} />
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
