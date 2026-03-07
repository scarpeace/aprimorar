import { useCallback, useEffect, useState } from "react"
import { Link, useNavigate } from "react-router-dom"
import { ActionErrorBanner } from "@/components/ui/action-error-banner"
import { Button } from "@/components/ui/button"
import { EmptyState } from "@/components/ui/empty-state"
import { ListPagination } from "@/components/ui/list-pagination"
import { ListPageHeader } from "@/components/ui/list-page-header"
import { PageErrorState } from "@/components/ui/page-error-state"
import { PageLoadingState } from "@/components/ui/page-loading-state"
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
import styles from "@/features/events/pages/EventsPage.module.css"

export function EventsPage() {
  const navigate = useNavigate()
  const [eventList, setEventList] = useState<EventResponse[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [deleteError, setDeleteError] = useState<string | null>(null)
  const [deletingId, setDeletingId] = useState<number | null>(null)
  const [page, setPage] = useState(0)
  const [size, setSize] = useState(20)
  const [totalElements, setTotalElements] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [isFirstPage, setIsFirstPage] = useState(true)
  const [isLastPage, setIsLastPage] = useState(true)

  const loadEvents = useCallback(async () => {
    try {
      setError(null)
      setLoading(true)

      const eventsRes = await eventsApi.list(page, size)
      const eventsPage: PageResponse<EventResponse> = eventsRes.data

      if (page > 0 && eventsPage.totalPages > 0 && page >= eventsPage.totalPages) {
        setPage(eventsPage.totalPages - 1)
        return
      }

      setEventList(eventsPage.content)
      setTotalElements(eventsPage.totalElements)
      setTotalPages(eventsPage.totalPages)
      setIsFirstPage(eventsPage.first)
      setIsLastPage(eventsPage.last)
    } catch (error) {
      console.error("Falha ao carregar eventos:", error)
      setError(getFriendlyErrorMessage(error))
    } finally {
      setLoading(false)
    }
  }, [page, size])

  useEffect(() => {
    loadEvents()
  }, [loadEvents])

  const handleDelete = async (event: EventResponse) => {
    if (!window.confirm(`Excluir evento "${event.title}"? Essa ação não pode ser desfeita.`)) {
      return
    }

    try {
      setDeleteError(null)
      setDeletingId(event.id)
      await eventsApi.delete(event.id)
      await loadEvents()
    } catch (error) {
      console.error("Falha ao excluir evento:", error)
      setDeleteError(getFriendlyErrorMessage(error))
    } finally {
      setDeletingId(null)
    }
  }

  if (loading) {
    return <PageLoadingState label="Carregando eventos..." />
  }

  if (error) {
    return (
      <PageErrorState
        title="Eventos"
        description="Gerencie horários, preços e atribuições."
        errorMessage={error}
        onRetry={loadEvents}
      />
    )
  }

  return (
    <div className={styles.page}>
      <ListPageHeader title="Eventos" description="Gerencie horários, preços e atribuições." actionLabel="Novo evento" actionTo="/events/new" />

      <ActionErrorBanner message={deleteError} />

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

      <ListPagination
        page={page}
        size={size}
        totalPages={totalPages}
        totalElements={totalElements}
        isFirstPage={isFirstPage}
        isLastPage={isLastPage}
        summaryLabel={`${totalElements} evento(s)`}
        selectId="events-page-size"
        onPageChange={setPage}
        onSizeChange={(nextSize) => {
          setSize(nextSize)
          setPage(0)
        }}
      />

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
