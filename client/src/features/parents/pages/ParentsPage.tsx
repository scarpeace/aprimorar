import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetParents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { Handshake } from "lucide-react";
import { useState } from "react";
import { ParentsTable } from "../components/ParentsTable";

export function ParentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const params = { page: currentPage, search: debouncedSearchTerm, archived: showArchived };

  const { data: parents, isPending, error } = useGetParents(params);

  return (
    <>
      <PageHeader
        description="Gerencie pais e responsáveis."
        title="Pais e Responsáveis"
        Icon={Handshake}
        backLink="/dashboard"
      />

      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-row">
          <ListSearchInput
            className="grow sm:mr-3"
            placeholder="Buscar responsável por nome, email ou CPF"
            ariaLabel="Buscar responsável"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ToggleSwitch
            label="Arquivados"
            tip="Mostrar responsáveis arquivados"
            toggled={showArchived}
            setToggle={setShowArchived}
          />
          <ButtonLink
            className="sm:ml-auto"
            to="/students/new"
            variant="success"
          >
            Novo Responsável
          </ButtonLink>
        </div>

      <ParentsTable
        parents={parents}
        isPending={isPending}
        error={error}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
      </div>
    </>
  );
}
