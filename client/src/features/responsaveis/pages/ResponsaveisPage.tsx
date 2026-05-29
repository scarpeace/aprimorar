import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import type { ResponsavelResponseDTO } from "@/kubb";
import { Handshake, Plus } from "lucide-react";
import { useState } from "react";
import { ResponsaveisTable } from "../components/ResponsaveisTable";
import { ResponsavelForm } from "../components/ResponsavelForm";

export function ResponsaveisPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedParent, setSelectedParent] = useState<ResponsavelResponseDTO | null>(null);

  const headerProps = {
    description: "Gerencie os responsáveis cadastrados no sistema.",
    title: "Responsáveis",
    Icon: Handshake,
    backLink: "/",
    iconBg: "info",
  } as const;

  const handleOpenForm = (responsavel?: ResponsavelResponseDTO) => {
    setSelectedParent(responsavel || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedParent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <h3 className="text-lg font-bold text-base-content">Responsaveis cadastrados</h3>
              <p className="text-sm text-base-content/60">
                Clique na linha para abrir os detalhes do cadastro.
              </p>
            </div>
            <Button
              className="sm:ml-auto"
              onClick={() => handleOpenForm()}
              variant="success"
            >
              <Plus className="mr-2 h-4 w-4" />
              Novo Responsavel
            </Button>
          </div>

          <ResponsaveisTable/>
        </section>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl border border-base-300 bg-base-100 shadow-2xl">
              <h3 className="mb-1 text-lg font-bold">
                {selectedParent ? "Editar Responsavel" : "Cadastrar Novo Responsavel"}
              </h3>
              <p className="mb-4 text-sm text-base-content/60">
                Atualize os dados principais do responsavel e mantenha os vinculos organizados.
              </p>
              <ResponsavelForm
                initialData={selectedParent}
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
