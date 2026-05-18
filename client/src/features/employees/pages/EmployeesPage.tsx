import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetEmployeesWithFinance } from "@/kubb";
import type { EmployeeResponseDTO } from "@/kubb";
import { useDateFilter } from "@/hooks/use-date-filter";
import { useDebounce } from "@/lib/shared/use-debounce";
import { FileUser, Plus } from "lucide-react";
import { useMemo, useState } from "react";
import { EmployeeKPIs } from "../components/EmployeeKPIs";
import { EmployeesTable } from "../components/EmployeesTable";
import { EmployeeForm } from "../components/EmployeeForm";

export function EmployeesPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [hidePaid, setHidePaid] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState<EmployeeResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  const { startDate, endDate } = useDateFilter();

  const employeesWithFinanceQuery = useGetEmployeesWithFinance({
    page: currentPage,
    search: debouncedSearchTerm,
    archived: showArchived,
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });

  const displayedEmployees = useMemo(() => {
    if (!employeesWithFinanceQuery.data || !hidePaid) {
      return employeesWithFinanceQuery.data;
    }

    return {
      ...employeesWithFinanceQuery.data,
      content: (employeesWithFinanceQuery.data.content ?? []).filter(
        (employee) => (employee.totalPending ?? 0) > 0,
      ),
    };
  }, [employeesWithFinanceQuery.data, hidePaid]);

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

  const handleShowArchivedChange = (value: boolean) => {
    setShowArchived(value);
    setCurrentPage(0);
  };

  const handleHidePaidChange = (value: boolean) => {
    setHidePaid(value);
    setCurrentPage(0);
  };

  const financeSummary = employeesWithFinanceQuery.data?.financeSummary;

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
          <div className="mb-4">
            <h3 className="text-lg font-bold text-base-content">Resumo financeiro dos colaboradores</h3>
            <p className="text-sm text-base-content/60">
              Indicadores consolidados respeitando o periodo selecionado nos filtros.
            </p>
          </div>

          <EmployeeKPIs
            totalEvents={financeSummary?.totalEvents ?? 0}
            totalPaid={financeSummary?.totalPaid ?? 0}
            totalUnpaid={financeSummary?.totalPending ?? 0}
          />
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_220ms_ease-out_both]">
          <div className="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div className="space-y-1">
              <h2 className="text-lg font-bold text-base-content">Busca e filtros</h2>
              <p className="text-sm text-base-content/60">
                Localize colaboradores por cadastro e alterne entre registros ativos e arquivados sem sair da listagem.
              </p>
            </div>

            <div className="rounded-2xl border border-success/15 bg-linear-to-r from-success/8 via-base-100 to-base-100 px-3 py-2 shadow-sm">
              <Button
                className="sm:ml-auto"
                onClick={() => handleOpenForm()}
                variant="success"
              >
                <Plus className="mr-2 h-4 w-4" />
                Novo Colaborador
              </Button>
            </div>
          </div>

          <div className="mt-4 flex flex-col gap-3 xl:flex-row xl:items-center">
            <ListSearchInput
              className="grow"
              placeholder="Buscar colaborador por nome, email ou CPF"
              ariaLabel="Buscar colaborador"
              value={searchTerm}
              onChange={setSearchTerm}
            />
            <div className="flex w-full flex-wrap items-center justify-between gap-3 xl:w-auto xl:justify-end">
              <ToggleSwitch
                label="Arquivados"
                tip="Mostrar colaboradores arquivados"
                toggled={showArchived}
                setToggle={handleShowArchivedChange}
                className="border-info/25 bg-base-100 shadow-sm checked:border-info checked:bg-info checked:text-info-content"
              />
              <ToggleSwitch
                label="Ocultar pagos"
                tip="Mostrar apenas colaboradores com pendencias no periodo"
                toggled={hidePaid}
                setToggle={handleHidePaidChange}
                className="border-warning/25 bg-base-100 shadow-sm checked:border-warning checked:bg-warning checked:text-warning-content"
              />
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="mb-4 flex flex-col gap-1 sm:flex-row sm:items-end sm:justify-between">
            <div>
              <h3 className="text-lg font-bold text-base-content">Colaboradores cadastrados</h3>
              <p className="text-sm text-base-content/60">
                Clique na linha para abrir os detalhes do cadastro.
              </p>
            </div>
          </div>

          <EmployeesTable
            employees={displayedEmployees}
            onPageChange={setCurrentPage}
            currentPage={currentPage}
            isPending={employeesWithFinanceQuery.isPending}
            error={employeesWithFinanceQuery.error}
          />
        </section>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl border border-base-300 bg-base-100 shadow-2xl">
              <h3 className="mb-1 text-lg font-bold">
                {selectedEmployee ? "Editar Colaborador" : "Cadastrar Novo Colaborador"}
              </h3>
              <p className="mb-4 text-sm text-base-content/60">
                Atualize dados pessoais, contato e funcao do colaborador para manter a operacao organizada.
              </p>
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
