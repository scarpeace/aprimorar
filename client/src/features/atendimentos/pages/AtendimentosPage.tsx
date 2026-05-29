import { useState } from "react";
import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { FileUser, Plus, UserCheck, UserCircle } from "lucide-react";
import type { AtendimentoResponseDTO } from "@/kubb";
import { AtendimentoForm } from "../components/AtendimentoForm";
import { AtendimentosTable } from "../components/AtendimentosTable";
import { KpiCard } from "@/components/ui/kpi-card";

export function AtendimentosPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEvent, setSelectedEvent] = useState<AtendimentoResponseDTO | null>(null);

  const headerProps = {
    description: "Gerencie cadastros e matrículas.",
    title: "Colaboradores",
    Icon: FileUser,
    backLink: "/",
  };

  const handleOpenForm = (atendimento?: AtendimentoResponseDTO) => {
    setSelectedEvent(atendimento || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedEvent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-3">
        <section className="rounded-2xl bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
          <div className="flex flex-row justify-between items-center gap-3">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Resumo dos Atendimentos</h3>
              <p className="text-sm text-base-content/60">
                Visão geral dos atendimentos por status e total.
              </p>
            </div>

            <div className="flex gap-3">
              <KpiCard
                label="Total Pendentes"
                value={10}
                Icon={UserCheck}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />

              <KpiCard
                label="Total Pagos"
                value={10}
                Icon={UserCheck}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />
              <KpiCard
                label="Total Desde o início"
                value={10}
                Icon={UserCircle}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />
            </div>
          </div>
        </section>

          <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
            <div className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
              <div>
                <h3 className="text-2xl font-bold text-base-content">Atendimentos cadastrados</h3>
                <p className="text-sm text-base-content/60">
                  Clique na linha para abrir os detalhes do cadastro.
                </p>
              </div>

              <Button className="sm:ml-auto" onClick={() => handleOpenForm()} variant="success">
                <Plus className="mr-2 h-4 w-4" />
                Novo Atendimento
              </Button>
            </div>

            <AtendimentosTable/>
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
              <AtendimentoForm
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
