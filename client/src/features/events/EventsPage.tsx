import { useEffect, useState } from "react"
import { Link } from "react-router-dom"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import type { EventResponse } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage, type PageResponse } from "@/services/api"
import styles from "@/features/events/EventsPage.module.css"

export function EventsPage() {
  const [eventList, setEventList] = useState<EventResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleteError, setDeleteError] = useState<string | null>(null)
  const [deletingId, setDeletingId] = useState<number | null>(null)

  const loadEvents = async () => {
    try {
      setError(null)
      setLoading(true)
      const eventsRes = await eventsApi.list()
      const eventsPage: PageResponse<EventResponse> = eventsRes.data
      setEventList(eventsPage.content)
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

  const handleDelete = async (event: EventResponse) => {
    if (!window.confirm(`Excluir evento "${event.title}"? Essa acao nao pode ser desfeita.`)) {
      return
    }

    try {
      setDeleteError(null)
      setDeletingId(event.id)
      await eventsApi.delete(event.id)
      setEventList((prev) => prev.filter((item) => item.id !== event.id))
    } catch (error) {
      console.error("Falha ao excluir evento:", error)
      setDeleteError(getFriendlyErrorMessage(error))
    } finally {
      setDeletingId(null)
    }
  }

  if (loading) {
    return <div>Carregando...</div>
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Eventos</h1>
          <p className="text-sm text-gray-600">Gerencie horarios, precos e atribuicoes.</p>
        </div>
        <EmptyState
          title="Nao foi possivel carregar"
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
          <p className="text-sm text-gray-600">Gerencie horarios, precos e atribuicoes.</p>
        </div>
        <Button asChild type="button">
          <Link to="/events/new">Novo evento</Link>
        </Button>
      </div>

      {deleteError ? (
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
          {deleteError}
        </div>
      ) : null}

      <div className={styles.tableWrap}>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Titulo</TableHead>
              <TableHead>Aluno</TableHead>
              <TableHead>Colaborador</TableHead>
              <TableHead>Data/Hora</TableHead>
              <TableHead>Preco</TableHead>
              <TableHead>Acoes</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {eventList.map((event) => (
              <TableRow key={event.id}>
                <TableCell>{event.title}</TableCell>
                <TableCell>{event.studentName}</TableCell>
                <TableCell>{event.employeeName}</TableCell>
                <TableCell>{event.startDateTime}</TableCell>
                <TableCell>{event.price}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/events/${event.id}`}>
                      Detalhes
                    </Link>
                    <Button
                      type="button"
                      variant="destructive"
                      size="sm"
                      onClick={() => handleDelete(event)}
                      disabled={deletingId === event.id}
                    >
                      {deletingId === event.id ? "Excluindo..." : "Excluir"}
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
          description="Quando voce cadastrar o primeiro evento, ele aparecera na tabela acima."
          actionLabel="Novo evento"
        />
      ) : null}
    </div>
  )
}
