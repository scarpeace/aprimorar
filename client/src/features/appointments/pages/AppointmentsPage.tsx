import { useState } from "react";
import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { CalendarCheck2, Plus } from "lucide-react";
import type { AtendimentoResponseDTO } from "@/kubb";
import { AppointmentForm } from "../components/AppointmentForm";
import { AppointmentsTable } from "../components/AppointmentsTable";

export function AppointmentsPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<AtendimentoResponseDTO | null>(null);

  const handleOpenForm = (event?: AtendimentoResponseDTO) => {
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
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Atendimentos cadastrados</h3>
              <p className="text-sm text-base-content/60">Use os filtros para localizar rapidamente por aluno, colaborador ou pendencias financeiras.</p>
            </div>

            <Button className="sm:ml-auto" onClick={() => handleOpenForm()} variant="success">
              <Plus className="mr-2 h-4 w-4" />
              Novo Atendimento
            </Button>
          </div>

          <AppointmentsTable />
        </section>

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
