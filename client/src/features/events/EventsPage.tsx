import { useEffect, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
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
import { eventContentLabels, type EventResponse } from "@/lib/schemas"
import { eventsApi, getFriendlyErrorMessage, type PageResponse } from "@/services/api"
import styles from "@/features/events/EventsPage.module.css"

export function EventsPage() {
  const navigate = useNavigate()
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
    if (!window.confirm(`Excluir evento "${event.title}"? Essa ação não pode ser desfeita.`)) {
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
    return <div>Carregando eventos...</div>
  }

  if (error) {
    return (
      <div className={styles.page}>
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Eventos</h1>
          <p className="text-sm text-gray-600">Gerencie horários, preços e atribuições.</p>
        </div>
        <EmptyState
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

      {deleteError ? (
        <div className="rounded-md border border-destructive/30 bg-destructive/5 p-3 text-sm">
          {deleteError}
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
              <TableHead>Preço</TableHead>
              <TableHead>Ações</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {eventList.map((event) => (
              <TableRow key={event.id}>
                <TableCell>{event.title}</TableCell>
                <TableCell>{event.studentName}</TableCell>
                <TableCell>{event.employeeName}</TableCell>
                <TableCell>{event.startDateTime}</TableCell>
                <TableCell>{eventContentLabels[event.content]}</TableCell>
                <TableCell>{event.price}</TableCell>
                <TableCell>
                  <div className="flex items-center gap-3">
                    <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/events/${event.id}`}>
                      Detalhes
                    </Link>
                    <Link className="text-sm font-medium text-blue-600 hover:underline" to={`/events/${event.id}/edit`}>
                      Editar
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
          description="Quando você cadastrar o primeiro evento, ele aparecerá na tabela acima."
          actionLabel="Novo evento"
          onAction={() => navigate("/events/new")}
        />
      ) : null}
    </div>
  )
}
