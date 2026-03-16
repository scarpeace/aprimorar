import { useState } from "react"
import { ErrorCard } from "@/components/ui/error-card"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { Pagination } from "@/components/ui/pagination"
import { ButtonLink } from "@/components/ui/button"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/events/EventsPage.module.css"
import { getFriendlyErrorMessage } from "@/services/api"
import { useEventsQuery } from "./hooks/use-events"

export function EventsPage() {
  const [currentPage, setCurrentPage] = useState(0)
  const pageSize = 10

  const {
    data: eventsResponse,
    isLoading: isLoadingEvents,
    isError: isErrorEvents,
    error: errorEvents,
    refetch,
  } = useEventsQuery(currentPage, pageSize)

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

  const pageInfo = eventsResponse?.page
  const totalElements = pageInfo?.totalElements ?? 0
  const totalPages = pageInfo?.totalPages ?? 0

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

      <Pagination
        currentPage={currentPage}
        totalElements={totalElements}
        totalPages={totalPages}
        currentElementsCount={eventsResponse?.content.length ?? 0}
        itemName="eventos"
        onPageChange={setCurrentPage}
      />
    </div>
  )
}
