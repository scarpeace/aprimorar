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

      <div className="flex items-center justify-between ml-auto">
        <div className="flex flex-1 items-center gap-2">
          <ListSearchInput
            placeholder="Buscar responsável por nome, email ou escola"
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
        </div>
        <ButtonLink to="/parents/new" variant="success">
          Novo responsável
        </ButtonLink>
      </div>

      <ParentsTable
        parents={parents}
        isPending={isPending}
        error={error}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </>
  );
}
