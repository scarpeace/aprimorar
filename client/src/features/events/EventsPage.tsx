import { useState } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ButtonLink } from "@/components/ui/button";
import styles from "@/features/events/EventsPage.module.css";
import { CalendarCheck2 } from "lucide-react";
import { EventTable } from "./components/EventTable";
import { useGetEvents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";

export function EventsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: events,
    isLoading: isEventsLoading,
    error: eventsError,
  } = useGetEvents({page: currentPage, search: debouncedSearchTerm});

  return (
    <div className={styles.page}>
      <PageHeader
        description="Gerencie os atendimentos."
        title="Atendimentos"
        Icon={CalendarCheck2}
        iconClassName="text-primary"
        iconBgClassName="bg-primary/15"
      >
        <div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ListSearchInput
            placeholder="Buscar atendimento por aluno, colaborador ou conteúdo"
            ariaLabel="Buscar atendimento"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ButtonLink className="sm:ml-auto" to="/events/new" variant="success">
            Novo atendimento
          </ButtonLink>
        </div>
      </PageHeader>

      <div className="app-table-wrap">
        <EventTable
          variant="page"
          context="student"
          data={events}
          isLoading={isEventsLoading}
          error={eventsError ?? null}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          itemName="eventos"
          renderActions={(event) => (
            <ButtonLink to={`/events/${event.eventId}`} size="sm" variant="outline">
              Detalhes
            </ButtonLink>
          )}
        />
      </div>
    </div>
  );
}
