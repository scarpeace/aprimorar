import { useState, useMemo } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ButtonLink, Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { CalendarCheck2, Filter } from "lucide-react";
import { EventsTable } from "../components/EventsTable";
import { useGetEvents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";

type DateFilter = "all" | "today" | "week";

export function EventsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [dateFilter, setDateFilter] = useState<DateFilter>("all");

  const queryParams = useMemo(() => {
    const params: Record<string, any> = { page: currentPage };

    if (debouncedSearchTerm) {
      params.search = debouncedSearchTerm;
    }

    if (dateFilter !== "all") {
      const start = new Date();
      start.setHours(0, 0, 0, 0);

      const end = new Date(start);
      if (dateFilter === "week") {
        end.setDate(start.getDate() + 7);
      }
      end.setHours(23, 59, 59, 999);

      params.startDate = start.toISOString();
      params.endDate = end.toISOString();
    }

    return params;
  }, [currentPage, debouncedSearchTerm, dateFilter]);

  const eventsQuery = useGetEvents(queryParams);

  const headerProps = {
    description: "Gerencie os atendimentos.",
    title: "Atendimentos",
    Icon: CalendarCheck2,
    backLink: "/",
  };

  const handleDateFilterChange = (filter: DateFilter) => {
    setDateFilter(filter);
    setCurrentPage(0);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-col sm:flex-row gap-3">
          <ListSearchInput
            className="grow"
            placeholder="Buscar atendimento por aluno, colaborador ou conteúdo"
            ariaLabel="Buscar atendimento"
            value={searchTerm}
            onChange={(val) => {
              setSearchTerm(val);
              setCurrentPage(0);
            }}
          />
          <ButtonLink className="sm:ml-auto" to="/events/new" variant="success">
            Novo atendimento
          </ButtonLink>
        </div>

        <div className="flex flex-wrap items-center gap-2 bg-base-200/50 p-2 rounded-lg">
          <div className="flex items-center gap-2 mr-2 text-sm font-medium text-base-content/70">
            <Filter className="h-4 w-4" />
            <span>Filtros Rápidos:</span>
          </div>
          <Button
            variant={dateFilter === "all" ? "primary" : "ghost"}
            size="sm"
            onClick={() => handleDateFilterChange("all")}
          >
            Todos
          </Button>
          <Button
            variant={dateFilter === "today" ? "primary" : "ghost"}
            size="sm"
            onClick={() => handleDateFilterChange("today")}
          >
            Hoje
          </Button>
          <Button
            variant={dateFilter === "week" ? "primary" : "ghost"}
            size="sm"
            onClick={() => handleDateFilterChange("week")}
          >
            Próximos 7 dias
          </Button>
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

