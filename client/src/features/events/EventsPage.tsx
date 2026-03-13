import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ErrorState } from "@/components/ui/error-state"
import { LoadingState } from "@/components/ui/loading-state"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { eventContentLabels, type EventResponse } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage} from "@/services/api"
import styles from "@/features/events/EventsPage.module.css"
import type { PageResponse } from "@/lib/schemas/page-response"

export function EventsPage() {
  const [eventList, setEventList] = useState<EventResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [archiveError, setArchiveError] = useState<string | null>(null)
  const [archiveId, setArchivingId] = useState<string | null>(null)

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

  const handleArchive = async (event: EventResponse) => {
    if (!window.confirm(`Excluir evento "${event.title}"? Essa ação não pode ser desfeita.`)) {
      return
    }

    try {
      setArchiveError(null)
      setArchivingId(event.id)
      await eventsApi.archive(event.id)
      setEventList((prev) => prev.filter((item) => item.id !== event.id))
    } catch (error) {
      console.error("Falha ao excluir evento:", error)
      setArchiveError(getFriendlyErrorMessage(error))
    } finally {
      setArchivingId(null)
    }
  }

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
        <Button asChild type="button">
          <Link to="/events/new">Novo evento</Link>
        </Button>
      </div>

      {archiveError ? (
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
          {archiveError}
        </div>
      ) : null}

      <div className={styles.tableWrap}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Título</TableHead>
              <TableHead>Aluno</TableHead>
              <TableHead>Colaborador</TableHead>
              <TableHead>Data/Hora</TableHead>
              <TableHead>Conteúdo</TableHead>
              <TableHead>Valor</TableHead>
              <TableHead>Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {eventList.map((event) => (
              <TableRow key={event.id}>
                <TableCell>{event.title}</TableCell>
                <TableCell>{event.studentName}</TableCell>
                <TableCell>{event.employeeName}</TableCell>
                <TableCell>{event.startDate}</TableCell>
                <TableCell>{eventContentLabels[event.content]}</TableCell>
                <TableCell>R$ {event.price}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/events/${event.id}`}>
                      Detalhes
                    </Link>
                    <Button
                      type="button"
                      variant="destructive"
                      size="sm"
                      onClick={() => handleArchive(event)}
                      disabled={archiveId === event.id}
                    >
                      {archiveId === event.id ? "Excluindo..." : "Excluir"}
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
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
