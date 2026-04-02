import { useState } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ButtonLink } from "@/components/ui/button";
import { CalendarCheck2 } from "lucide-react";
import { EventsTable } from "../components/EventsTable";
import { useGetEvents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";

export function EventsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: events,
    isPending: isEventsPending,
    error: eventsError,
  } = useGetEvents({ page: currentPage, search: debouncedSearchTerm });

  return (
    <div className="flex flex-col gap-7">
      <PageHeader
        description="Gerencie os atendimentos."
        title="Atendimentos"
        Icon={CalendarCheck2}
      >
        {/*<div className="flex w-full flex-col gap-2 sm:flex-row sm:items-center">
          <ButtonLink className="sm:ml-auto" to="/events/new" variant="success">
            Novo atendimento
          </ButtonLink>
        </div>*/}
      </PageHeader>

      <div className="flex flex-col gap-2 w-full">
        <div className="flex flex-row">
          <ListSearchInput
            className="grow"
            placeholder="Buscar atendimento por aluno, colaborador ou conteúdo"
            ariaLabel="Buscar atendimento"
            value={searchTerm}
            onChange={setSearchTerm}
        />
        <ButtonLink className="sm:ml-auto" to="/events/new" variant="success">
            Novo atendimento
          </ButtonLink>
          </div>
        <EventsTable
          eventsPage={events}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          isPending={isEventsPending}
          error={eventsError}
        />
      </div>
    </div>
  );
}
