import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetEmployees } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { FileUser } from "lucide-react";
import { useState } from "react";
import { EmployeesTable } from "../components/EmployeesTable";

export function EmployeesPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const employeesQuery = useGetEmployees({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  return (
    <>
      <PageHeader
        description="Gerencie cadastros e matrículas."
        title="Colaboradores"
        Icon={FileUser}
        backLink="/dashboard"
      />
      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-row">
          <ListSearchInput
            className="grow sm:mr-3"
            placeholder="Buscar colaborador por nome, email ou CPF"
            ariaLabel="Buscar colaborador"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <ToggleSwitch
            label="Arquivados"
            tip="Mostrar colaboradores arquivados"
            toggled={showArchived}
            setToggle={setShowArchived}
          />
          <ButtonLink
            className="sm:ml-auto"
            to="/students/new"
            variant="success"
          >
            Novo Colaborador
          </ButtonLink>
        </div>

      <EmployeesTable
        employees={employeesQuery.data}
        onPageChange={setCurrentPage}
        currentPage={currentPage}
        isPending={employeesQuery.isPending}
        error={employeesQuery.error}
        />
        </div>
    </>
  );
}
