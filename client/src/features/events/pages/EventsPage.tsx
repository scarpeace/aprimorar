import { useState } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { CalendarCheck2, Plus } from "lucide-react";
import { EventsTable } from "../components/EventsTable";
import { useGetEvents } from "@/kubb";
import type { EventResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { EventForm } from "../components/EventForm";
import { useSearchParams } from "react-router-dom";
import { getStartOfMonthISO, getEndOfMonthISO } from "@/lib/utils/dateFormater";
import { DateRangeInput } from "@/components/ui/date-range-input";

export function EventsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [searchParams, setSearchParams] = useSearchParams();
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<EventResponseDTO | null>(null);

  const startDateStr = searchParams.get("startDate") ?? getStartOfMonthISO();
  const endDateStr = searchParams.get("endDate") ?? getEndOfMonthISO();

  const startDate = new Date(startDateStr);
  const endDate = new Date(endDateStr);

  const eventsQuery = useGetEvents({
    page: currentPage,
    search: debouncedSearchTerm,
    sort: ["startDate,desc", "id,asc"],
    startDate: startDateStr,
    endDate: endDateStr
  });

  const handleOpenForm = (event?: EventResponseDTO) => {
    setSelectedEvent(event || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedEvent(null);
    setIsFormOpen(false);
  };

  const handleStartDateChange = (date: Date) => {
    const newParams = new URLSearchParams(searchParams);
    date.setHours(0, 0, 0, 0);
    newParams.set("startDate", date.toISOString());
    setSearchParams(newParams);
    setCurrentPage(0);
  };

  const handleEndDateChange = (date: Date) => {
    const newParams = new URLSearchParams(searchParams);
    date.setHours(23, 59, 59, 999);
    newParams.set("endDate", date.toISOString());
    setSearchParams(newParams);
    setCurrentPage(0);
  };

  return (
    <PageLayout description="Gerencie os atendimentos." title="Atendimentos" Icon={CalendarCheck2} backLink="/">
      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-col lg:flex-row gap-3 items-center">
          <ListSearchInput
            className="grow w-full lg:w-auto"
            placeholder="Buscar atendimento por aluno, colaborador ou conteúdo"
            ariaLabel="Buscar atendimento"
            value={searchTerm}
            onChange={(val) => {
              setSearchTerm(val);
              setCurrentPage(0);
            }}
          />

          <DateRangeInput
            startDate={startDate}
            endDate={endDate}
            onStartDateChange={handleStartDateChange}
            onEndDateChange={handleEndDateChange}
          />

          <Button
            className="w-full lg:w-auto lg:ml-auto"
            onClick={() => handleOpenForm()}
            variant="success"
          >
            <Plus className="h-4 w-4 mr-2" />
            Novo atendimento
          </Button>
        </div>

        <EventsTable
          eventsPage={eventsQuery.data}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          isPending={eventsQuery.isPending}
          error={eventsQuery.error}
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
