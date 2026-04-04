import { ButtonLink } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { PageHeader } from "@/components/ui/page-header";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { useGetEmployees } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { UserCog } from "lucide-react";
import { useState } from "react";
import { EmployeesTable } from "../components/EmployeesTable";

export function EmployeesPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);

  const {
    data: employees,
    isLoading: isEmployeesLoading,
    error: employeesError,
    refetch: refetchEmployees,
  } = useGetEmployees({
    page: currentPage,
    size: 8,
    search: debouncedSearchTerm,
  });


  return (
    <>
      <PageHeader
        description="Gerencie colaboradores."
        title="Colaboradores"
        Icon={UserCog}
        backLink="/dashboard"
      />
      <div className="flex items-center justify-between ml-auto gap-6">
        <ListSearchInput
          className="grow"
 // TODO: implementar a busca por CPF no backend
          placeholder="Buscar colaborador por nome, email ou CPF"
          ariaLabel="Buscar colaborador"
          value={searchTerm}
          onChange={setSearchTerm}
        />
        <ToggleSwitch
          className="justify-self-start"
          label="Arquivados"
          tip="Mostrar colaboradors arquivados"
          toggled={showArchived}
          setToggle={setShowArchived}
        />
        <ButtonLink to="/employees/new" variant="success">
          Novo colaborador
        </ButtonLink>
      </div>
      <EmployeesTable
        employees={employees}
        onPageChange={setCurrentPage}
        currentPage={currentPage}
        isPending={isEmployeesLoading}
        error={employeesError}
      />
    </>
  );
}
