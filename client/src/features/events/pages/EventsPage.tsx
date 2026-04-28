import { useState, useMemo } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { CalendarCheck2, Filter, Plus } from "lucide-react";
import { EventsTable } from "../components/EventsTable";
import { useGetEvents } from "@/kubb";
import type { EventResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { EventForm } from "../components/EventForm";

type DateFilter = "all" | "today" | "week";

export function EventsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [dateFilter, setDateFilter] = useState<DateFilter>("all");
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<EventResponseDTO | null>(null);

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

  const handleOpenForm = (event?: EventResponseDTO) => {
    setSelectedEvent(event || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedEvent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout description="Gerencie os atendimentos." title="Atendimentos" Icon={CalendarCheck2} backLink="/">
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
          <Button
            className="sm:ml-auto"
            onClick={() => handleOpenForm()}
            variant="success"
          >
            <Plus className="h-4 w-4 mr-2" />
            Novo atendimento
          </Button>
        </div>

        {/*<div className="flex flex-wrap items-center gap-2 bg-base-200/50 p-2 rounded-lg">
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
        </div>*/}

        <EventsTable
          eventsPage={eventsQuery.data}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          isPending={eventsQuery.isPending}
          error={eventsQuery.error}
          onEdit={(event) => handleOpenForm(event)}
        />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-4xl">
              <h3 className="font-bold text-lg mb-4">
                {selectedEvent ? "Editar Atendimento" : "Cadastrar Novo Atendimento"}
              </h3>
              <EventForm
                initialData={selectedEvent}
                onSuccess={handleCloseForm}
                onCancel={handleCloseForm}
              />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
