import { useQuery } from "@tanstack/react-query"
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
    data: eventsResponse,
    isLoading: isLoadingEvents,
    isError: isErrorEvents,
    error: errorEvents,
    refetch,
  } = useQuery({
    queryKey: [...queryKeys.events, EVENTS_LIST_PARAMS],
    queryFn: async () => {
      return eventsApi.list(
        EVENTS_LIST_PARAMS.page,
        EVENTS_LIST_PARAMS.size,
        EVENTS_LIST_PARAMS.sortBy
      )
    },
  })

  if (isLoadingEvents) {
    return <PageLoading message="Carregando eventos..." />
  }

  if (isErrorEvents) {
    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(errorEvents)} onAction={refetch} />
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
        <EventsTable
          variant="eventsPage"
          eventsPage={eventsResponse!}
          loading={isLoadingEvents}
          error={errorEvents ? getFriendlyErrorMessage(errorEvents) : ""}
        />
      </div>
    </div>
  )
}
