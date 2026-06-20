import { Button } from "@/components/button.tsx";
import { Modal } from "@/components/modal.tsx";
import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import type { ResponsavelResponseDTO } from "@/kubb";
import { Handshake, Plus } from "lucide-react";
import { useState } from "react";
import { ResponsaveisTable } from "../../components/Responsavel/ResponsaveisTable.tsx";
import { ResponsavelForm } from "../../components/Responsavel/ResponsavelForm.tsx";

export function ResponsaveisPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedResponsavel, setSelectedResponsavel] = useState<ResponsavelResponseDTO | null>(null);

  const headerProps = {
    description: "Gerencie os responsáveis cadastrados no sistema.",
    title: "Responsáveis",
    Icon: Handshake,
    iconBg: "info",
  } as const;

  const handleOpenForm = (responsavel?: ResponsavelResponseDTO) => {
    setSelectedResponsavel(responsavel || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedResponsavel(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <h3 className="text-lg font-bold text-base-content">Responsáveis cadastrados</h3>
              <p className="text-sm text-base-content/60">Clique na linha para abrir os detalhes do cadastro.</p>
            </div>
            <Button className="sm:ml-auto" onClick={() => handleOpenForm()} variant="success">
              <Plus className="mr-2 h-4 w-4" />
              Novo Responsável
            </Button>
          </div>

          <ResponsaveisTable openForm={() => setIsFormOpen(true)} />
        </section>

        <Modal
          isOpen={isFormOpen}
          onClose={handleCloseForm}
          title={selectedResponsavel ? "Editar Responsável" : "Cadastrar Novo Responsável"}
          description="Atualize os dados principais do responsável e mantenha os vínculos organizados."
          size="md"
        >
          <ResponsavelForm
            initialData={selectedResponsavel}
            onSuccess={handleCloseForm}
            onCancel={handleCloseForm}
          />
        </Modal>
      </div>
    </PageLayout>
  );
}
