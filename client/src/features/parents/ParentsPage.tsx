import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { Pagination } from "@/components/ui/pagination";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetParents } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { GraduationCap, Handshake } from "lucide-react";
import { useState } from "react";
import { ParentsTable } from "./components/ParentsTable";

export function ParentsPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const { data: parentsResponse, isPending, error, } = useGetParents({
    page: currentPage,
    size: 8,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  const { content: parents, ...pageMetadata } = parentsResponse || {}

  return (
    <>
      <PageHeader
        description="Gerencie pais e responsáveis."
        title="Pais e Responsáveis"
        Icon={Handshake}
      >
        <div className="flex items-center ml-auto gap-6">
          <ListSearchInput
            className="w-80 sm:w-96"
            placeholder="Buscar aluno por nome, email ou escola"
            ariaLabel="Buscar aluno"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ToggleSwitch
            label="Arquivados"
            tip="Mostrar alunos arquivados"
            toggled={showArchived}
            setToggle={setShowArchived}
          />
          <ButtonLink to="/parents/new" variant="success">
            Novo aluno
          </ButtonLink>
        </div>
      </PageHeader>

      <ParentsTable
        parents={parents}
         isPending={isPending}
          error={error}
      >
          <Pagination
            paginationData={pageMetadata}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
      </ParentsTable>
    </>
  );
}
