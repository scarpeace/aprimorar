import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
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

  const parentsQuery = useGetParents({
    page: currentPage, search: debouncedSearchTerm, archived: showArchived
  });

  const headerProps = {
    description: "Gerencie os responsáveis cadastrados no sistema.",
    title: "Responsáveis",
    Icon: Handshake,
    backLink: "/",
  };

  return (
    <PageLayout {...headerProps}>
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
            to="/parents/new"
            variant="success"
          >
            Cadastrar responsável
          </ButtonLink>
        </div>

        <ParentsTable
          parents={parentsQuery.data}
          isPending={parentsQuery.isPending}
          error={parentsQuery.error}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </div>
    </PageLayout>
  );
}
