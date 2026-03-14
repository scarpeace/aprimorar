import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import type { EventResponse } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"
import styles from "@/features/events/EventsPage.module.css"
import type { PageResponse } from "@/lib/schemas/page-response"
import { EventsTable } from "@/components/ui/events-table"

export function EventsPage() {
  const [eventList, setEventList] = useState<EventResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const loadEvents = async () => {
    try {
      setError(null)
      setLoading(true)
      const eventsRes: PageResponse<EventResponse> = await eventsApi.list()
      setEventList(eventsRes.content)
    } catch (error) {
      console.error("Falha ao carregar eventos:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadEvents()
  }, [])

  if (loading) {
    return <LoadingState message="Carregando eventos..." />
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Eventos</h1>
          <p className="text-sm text-gray-600">Gerencie horários, preços e atribuições.</p>
        </div>
        <ErrorState
          title="Não foi possível carregar"
          description={error}
          actionLabel="Tentar novamente"
          onAction={loadEvents}
        />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <div className={styles.header}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Eventos</h1>
          <p className="text-sm text-gray-600">Gerencie horários, preços e atribuições.</p>
        </div>
        <Button asChild variant="success">
          <Link to="/events/new">Novo evento</Link>
        </Button>
      </div>

      <div className={styles.tableWrap}>
        <EventsTable variant="studentPage" events={eventList} />
      </div>

      {eventList.length === 0 ? (
        <EmptyState
          title="Nenhum evento cadastrado"
          description="Quando você cadastrar o primeiro evento, ele aparecerá na tabela acima."
          actionLabel="Novo evento"
        />
      ) : null}
    </div>
  )
}
