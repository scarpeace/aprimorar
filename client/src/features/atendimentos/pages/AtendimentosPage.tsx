import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Modal } from "@/components/ui/modal";
import { PageLayout } from "@/components/layout/PageLayout";
import { BellElectric, Plus } from "lucide-react";
import { AtendimentoForm } from "../components/AtendimentoForm";
import { AtendimentosTable } from "../components/AtendimentosTable";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter";
import type { AtendimentoResponse } from "@/kubb";

export function AtendimentosPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedAtendimento, setSelectedAtendimento] = useState<AtendimentoResponse | null>(null);
  const { startDate, endDate, ...dateFilter } = usePageDateFilter();


  const headerProps = {
    description: "Gerencie aulas e atendimentos.",
    title: "Atendimentos",
    Icon: BellElectric,
    iconBg: "accent",
  } as const;

  const handleOpenForm = (atendimento?: AtendimentoResponse) => {
    setSelectedAtendimento(atendimento || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedAtendimento(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-3">
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

            <AtendimentosTable startDate={startDate} endDate={endDate}/>
          </section>


        <Modal
          isOpen={isFormOpen}
          onClose={handleCloseForm}
          title={selectedAtendimento ? "Editar Atendimento" : "Cadastrar Novo Atendimento"}
          description="Defina aluno, colaborador, horario e valores do atendimento para manter agenda e financeiro sincronizados."
          size="lg"
        >
          <AtendimentoForm
            initialData={selectedAtendimento}
            onSuccess={handleCloseForm}
            onCancel={handleCloseForm}
          />
        </Modal>
      </div>

      <PageDateFilterWidget startDate={startDate} endDate={endDate} {...dateFilter} />
    </PageLayout>
  );
}
