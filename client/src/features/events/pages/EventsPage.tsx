import { useState } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { CalendarCheck2, Plus } from "lucide-react";
import type { EventResponseDTO } from "@/kubb";
import { EventForm } from "../components/EventForm";
import { DateRangeInput } from "@/components/ui/date-range-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useEventsFilters } from "../hooks/use-events-filters";
import { EventsListSection } from "../components/EventsListSection";

export function EventsPage() {
  const {
    search,
    startDate,
    endDate,
    hideCharged,
    hidePaid,
    page,
    handleSearchChange,
    handleStartDateChange,
    handleEndDateChange,
    handleHideChargedToggle,
    handleHidePaidToggle,
    handlePageChange,
  } = useEventsFilters();

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<EventResponseDTO | null>(null);

  const handleOpenForm = (event?: EventResponseDTO) => {
    setSelectedEvent(event || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedEvent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout description="Gerencie os atendimentos e operações financeiras." title="Atendimentos" Icon={CalendarCheck2} backLink="/">
      <div className="flex flex-col gap-4 w-full">
        {/* Toolbar */}
        <div className="flex flex-wrap items-center gap-4 bg-base-200/50 p-4 rounded-xl border border-base-300 shadow-sm">
          <ListSearchInput
            className="flex-1 min-w-[280px]"
            placeholder="Buscar por aluno, colaborador ou conteúdo..."
            ariaLabel="Buscar atendimento"
            value={search}
            onChange={handleSearchChange}
          />

          <div className="flex items-center gap-1 bg-base-100 px-3 py-1 rounded-lg border border-base-300">
            <ToggleSwitch
              label="Cobrança Pendente"
              toggled={hideCharged}
              setToggle={handleHideChargedToggle}
              tip="Mostrar apenas eventos onde o aluno ainda não foi cobrado"
            />
            <div className="divider divider-horizontal mx-0"></div>
            <ToggleSwitch
              label="Pagamento Pendente"
              toggled={hidePaid}
              setToggle={handleHidePaidToggle}
              tip="Mostrar apenas eventos onde o colaborador ainda não foi pago"
            />
          </div>

          <DateRangeInput
            startDate={startDate}
            endDate={endDate}
            onStartDateChange={handleStartDateChange}
            onEndDateChange={handleEndDateChange}
          />

          <Button
            className="w-full sm:w-auto"
            onClick={() => handleOpenForm()}
            variant="success"
          >
            <Plus className="h-4 w-4 mr-2" />
            Novo atendimento
          </Button>
        </div>

        <EventsListSection
          search={search}
          startDate={startDate}
          endDate={endDate}
          hideCharged={hideCharged}
          hidePaid={hidePaid}
          page={page}
          onPageChange={handlePageChange}
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
