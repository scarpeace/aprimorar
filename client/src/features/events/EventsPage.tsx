import { useState } from "react"
import { ListSearchInput } from "@/components/ui/list-search-input"
import { PageHeader } from "@/components/ui/page-header"
import { ButtonLink } from "@/components/ui/button"
import { EventsTable } from "@/features/events/components/EventsTable"
import styles from "@/features/events/EventsPage.module.css"
import { useDebounce } from "@/hooks/use-debounce"

export function EventsPage() {
  const [searchTerm, setSearchTerm] = useState("")
  const debouncedSearchTerm = useDebounce(searchTerm, 500)

  return (
    <div className={styles.page}>
      <PageHeader description="Gerencie horários, preços e atribuições." title="Eventos">
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar evento por aluno, colaborador ou conteúdo"
            ariaLabel="Buscar evento"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ButtonLink className="sm:ml-auto" to="/events/new" variant="success">
            Novo evento
          </ButtonLink>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <EventsTable
          variant="eventsPage"
          searchTerm={debouncedSearchTerm}
        />
      </div>
    </div>
  )
}
