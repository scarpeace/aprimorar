import { useState } from "react";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { CalendarCheck2, Plus, RotateCcw } from "lucide-react";
import type { AppointmentResponseDTO } from "@/kubb";
import { AppointmentForm } from "../components/AppointmentForm";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useAppointmentsFilters } from "../hooks/use-appointments-filters";
import { AppointmentsListSection } from "../components/AppointmentsListSection";

export function AppointmentsPage() {
  const {
    search,
    hideCharged,
    hidePaid,
    page,
    hasFilters,
    handleClearFilters,
    handleSearchChange,
    handleHideChargedToggle,
    handleHidePaidToggle,
    handlePageChange,
  } = useAppointmentsFilters();

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<AppointmentResponseDTO | null>(null);

  const handleOpenForm = (event?: AppointmentResponseDTO) => {
    setSelectedEvent(event || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedEvent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout description="Gerencie os atendimentos e operações financeiras." title="Atendimentos" Icon={CalendarCheck2} backLink="/">
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_220ms_ease-out_both]">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div className="space-y-1">
              <h2 className="text-lg font-bold text-base-content">Busca e filtros</h2>
              <p className="text-sm text-base-content/60">
                Filtre por participante, periodo e pendencias financeiras para localizar atendimentos com rapidez.
              </p>
            </div>

            <div className="rounded-2xl border border-success/15 bg-linear-to-r from-success/8 via-base-100 to-base-100 px-3 py-2 shadow-sm">
              <Button className="sm:ml-auto" onClick={() => handleOpenForm()} variant="success">
                <Plus className="mr-2 h-4 w-4" />
                Novo atendimento
              </Button>
            </div>
          </div>

          <div className="mt-4 flex items-center flex-row mb-3 gap-3">
            <ListSearchInput
              placeholder="Buscar por aluno, colaborador, conteúdo ou descrição"
              ariaLabel="Buscar atendimento"
              value={search}
              onChange={handleSearchChange}
            />
            </div>

            <div className="flex flex-col gap-3 xl:flex-row xl:items-center xl:justify-between">
              <div className="flex flex-wrap items-center gap-3">
                <ToggleSwitch
                  label="Cobrança Pendente"
                  toggled={hideCharged}
                  setToggle={handleHideChargedToggle}
                  tip="Mostrar apenas eventos onde o aluno ainda não foi cobrado"
                  className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
                />
                <ToggleSwitch
                  label="Pagamento Pendente"
                  toggled={hidePaid}
                  setToggle={handleHidePaidToggle}
                  tip="Mostrar apenas eventos onde o colaborador ainda não foi pago"
                  className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
                />
              </div>

              {hasFilters ? (
                <Button variant="ghost" onClick={handleClearFilters} className="gap-2 self-start xl:self-auto">
                  <RotateCcw className="h-4 w-4" />
                  Limpar filtros
                </Button>
              ) : null}
            </div>
        </section>

        <AppointmentsListSection
          page={page}
          search={search}
          hideCharged={hideCharged}
          hidePaid={hidePaid}
          onPageChange={handlePageChange}
        />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-4xl border border-base-300 bg-base-100 shadow-2xl">
              <h3 className="mb-1 text-lg font-bold">
                {selectedEvent ? "Editar Atendimento" : "Cadastrar Novo Atendimento"}
              </h3>
              <p className="mb-4 text-sm text-base-content/60">
                Defina aluno, colaborador, horario e valores do atendimento para manter agenda e financeiro sincronizados.
              </p>
              <AppointmentForm
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
