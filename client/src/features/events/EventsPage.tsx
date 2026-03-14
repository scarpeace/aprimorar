import { useQuery } from "@tanstack/react-query"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { ButtonLink } from "@/components/ui/button"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/events/EventsPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"

const EVENTS_LIST_PARAMS = { page: 0, size: 20, sortBy: "startDate" }

export function EventsPage() {
  const {
    data: eventList = [],
    isLoading,
    isError,
    error,
    refetch,
  } = useQuery({
    queryKey: queryKeys.events.list(EVENTS_LIST_PARAMS),
    queryFn: async () => {
      const eventsRes = await eventsApi.list(
        EVENTS_LIST_PARAMS.page,
        EVENTS_LIST_PARAMS.size,
        EVENTS_LIST_PARAMS.sortBy
      )

      return eventsRes.content
    },
  })

  if (isLoading) {
    return <PageLoading message="Carregando eventos..." />
  }

  if (isError) {
    return (
      <div className={styles.page}>
        <PageHeader title="Eventos" description="Gerencie horários, preços e atribuições." />
        <ErrorCard description={getFriendlyErrorMessage(error)} onAction={refetch} />
      </div>
    )
  }

  return (
    <div className={styles.page}>
      <PageHeader description="Gerencie horários, preços e atribuições." title="Eventos">
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar evento por aluno, colaborador ou conteúdo"
            ariaLabel="Buscar evento"
          />
          <ButtonLink className="sm:ml-auto" to="/events/new" variant="success">
            Novo evento
          </ButtonLink>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <EventsTable variant="eventsPage" events={eventList} />
      </div>

      {eventList.length === 0 ? (
        <EmptyCard
          title="Nenhum evento cadastrado"
          description="Quando você cadastrar o primeiro evento, ele aparecerá na tabela acima."
          action={
            <ButtonLink to="/events/new" variant="secondary">
              Novo evento
            </ButtonLink>
          }
        />
      ) : null}
    </div>
  )
}
