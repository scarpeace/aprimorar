import { useState } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ButtonLink } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { CalendarCheck2 } from "lucide-react";
import { EventsTable } from "../components/EventsTable";
import { useGetEvents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";

export function EventsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);

  const eventsQuery = useGetEvents({ page: currentPage, search: debouncedSearchTerm });

  const headerProps = {
    description: "Gerencie os atendimentos.",
    title: "Atendimentos",
    Icon: CalendarCheck2,
    backLink: "/dashboard",
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-3 w-full">
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
          eventsPage={eventsQuery.data}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          isPending={eventsQuery.isPending}
          error={eventsQuery.error}
        />
      </div>
    </PageLayout>
  );
}
