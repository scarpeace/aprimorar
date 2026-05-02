import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetParents } from "@/kubb";
import type { ParentResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
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
      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-col justify-between sm:flex-row gap-3">
          <ListSearchInput
            className="grow sm:mr-3"
            placeholder="Buscar responsável por nome, email ou CPF"
            ariaLabel="Buscar responsável"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <div className="flex justify-between sm:justify-end w-full sm:w-auto items-center gap-3">
            {/*<ToggleSwitch
              label="Arquivados"
              tip="Mostrar responsáveis arquivados"
              toggled={showArchived}
              setToggle={setShowArchived}
            />*/}
            <Button
              className="sm:ml-auto"
              onClick={() => handleOpenForm()}
              variant="success"
            >
              <Plus className="h-4 w-4 mr-2" />
              Novo Responsável
            </Button>
          </div>
        </div>

        <ParentsTable
          parents={parentsQuery.data}
          isPending={parentsQuery.isPending}
          error={parentsQuery.error}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          onEdit={(parent) => handleOpenForm(parent)}
        />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl">
              <h3 className="font-bold text-lg mb-4">
                {selectedParent ? "Editar Responsável" : "Cadastrar Novo Responsável"}
              </h3>
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
