import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetParents } from "@/kubb";
import type { ParentResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce.ts";
import { Handshake, Plus } from "lucide-react";
import { useState } from "react";
import { ParentsTable } from "../components/ParentsTable";
import { ParentForm } from "../components/ParentForm";

export function ParentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedParent, setSelectedParent] = useState<ParentResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const parentsQuery = useGetParents({
    page: currentPage, search: debouncedSearchTerm, archived: showArchived
  });

  const headerProps = {
    description: "Gerencie os responsáveis cadastrados no sistema.",
    title: "Responsáveis",
    Icon: Handshake,
    backLink: "/",
  };

  const handleOpenForm = (parent?: ParentResponseDTO) => {
    setSelectedParent(parent || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedParent(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_220ms_ease-out_both]">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div className="space-y-1">
              <h2 className="text-lg font-bold text-base-content">Busca e filtros</h2>
              <p className="text-sm text-base-content/60">
                Consulte rapidamente os responsaveis cadastrados e alterne a visualizacao do historico arquivado.
              </p>
            </div>

            <div className="rounded-2xl border border-success/15 bg-linear-to-r from-success/8 via-base-100 to-base-100 px-3 py-2 shadow-sm">
              <Button
                className="sm:ml-auto"
                onClick={() => handleOpenForm()}
                variant="success"
              >
                <Plus className="mr-2 h-4 w-4" />
                Novo Responsavel
              </Button>
            </div>
          </div>

          <div className="mt-4 flex flex-col gap-3 xl:flex-row xl:items-center">
            <ListSearchInput
            className="grow"
            placeholder="Buscar responsável por nome, email ou CPF"
            ariaLabel="Buscar responsável"
            value={searchTerm}
            onChange={setSearchTerm}
          />
            <div className="flex w-full flex-wrap items-center justify-between gap-3 xl:w-auto xl:justify-end">
              <ToggleSwitch
              label="Arquivados"
              tip="Mostrar responsáveis arquivados"
              toggled={showArchived}
              setToggle={setShowArchived}
                className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
              />
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <h3 className="text-lg font-bold text-base-content">Responsaveis cadastrados</h3>
              <p className="text-sm text-base-content/60">
                Clique na linha para abrir os detalhes do cadastro.
              </p>
            </div>
          </div>

          <ParentsTable
            parents={parentsQuery.data}
            isPending={parentsQuery.isPending}
            error={parentsQuery.error}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
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
              <ParentForm
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
