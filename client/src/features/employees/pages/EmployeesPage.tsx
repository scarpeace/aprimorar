import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetEmployees } from "@/kubb";
import type { EmployeeResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/shared/use-debounce";
import { FileUser, Plus } from "lucide-react";
import { useState } from "react";
import { EmployeesTable } from "../components/EmployeesTable";
import { EmployeeForm } from "../components/EmployeeForm";

export function EmployeesPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState<EmployeeResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const employeesQuery = useGetEmployees({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
  });

  const headerProps = {
    description: "Gerencie cadastros e matrículas.",
    title: "Colaboradores",
    Icon: FileUser,
    backLink: "/",
  };

  const handleOpenForm = (employee?: EmployeeResponseDTO) => {
    setSelectedEmployee(employee || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedEmployee(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-3 w-full">
        <div className="flex flex-col justify-between sm:flex-row gap-3">
          <ListSearchInput
            className="grow"
            placeholder="Buscar colaborador por nome, email ou CPF"
            ariaLabel="Buscar colaborador"
            value={searchTerm}
            onChange={setSearchTerm}
          />
          <div className="flex justify-between sm:justify-end w-full sm:w-auto items-center gap-3">
            {/*<ToggleSwitch
              label="Arquivados"
              tip="Mostrar colaboradores arquivados"
              toggled={showArchived}
              setToggle={setShowArchived}
            />*/}
            <Button
              className="sm:ml-auto"
              onClick={() => handleOpenForm()}
              variant="success"
            >
              <Plus className="h-4 w-4 mr-2" />
              Novo Colaborador
            </Button>
          </div>
        </div>

        <EmployeesTable
          employees={employeesQuery.data}
          onPageChange={setCurrentPage}
          currentPage={currentPage}
          isPending={employeesQuery.isPending}
          error={employeesQuery.error}
          onEdit={(employee) => handleOpenForm(employee)}
        />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl">
              <h3 className="font-bold text-lg mb-4">
                {selectedEmployee ? "Editar Colaborador" : "Cadastrar Novo Colaborador"}
              </h3>
              <EmployeeForm
                initialData={selectedEmployee}
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
