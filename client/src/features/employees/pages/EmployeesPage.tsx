import { Button } from "@/components/ui/button";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { ToggleSwitch } from "@/components/ui/toggle-switch";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetColaboradores, type ColaboradorResponseDTO } from "@/kubb";
import { useDebounce } from "@/lib/hooks/use-debounce.ts";
import { CheckCircle, FileUser, Plus, UserCheck, UserCircle } from "lucide-react";
import { useMemo, useState } from "react";
import { EmployeesTable } from "../components/EmployeesTable";
import { EmployeeForm } from "../components/EmployeeForm";
import { KpiCard } from "@/components/ui/kpi-card";

export function EmployeesPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [showArchived, setShowArchived] = useState(false);
  const [hidePaid, setHidePaid] = useState(false);
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState<ColaboradorResponseDTO | null>(null);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const colaboradoresQuery = useGetColaboradores({
    page: currentPage,
    busca: debouncedSearchTerm,
    arquivado: showArchived,
  });

  // const displayedEmployees = useMemo(() => {
  //   if (!employeesWithFinanceQuery.data || !hidePaid) {
  //     return employeesWithFinanceQuery.data;
  //   }

  //   return {
  //     ...employeesWithFinanceQuery.data,
  //     content: (employeesWithFinanceQuery.data.content ?? []).filter(
  //       (employee) => (employee.totalPending ?? 0) > 0,
  //     ),
  //   };
  // }, [employeesWithFinanceQuery.data, hidePaid]);

  const headerProps = {
    description: "Gerencie cadastros e matrículas.",
    title: "Colaboradores",
    Icon: FileUser,
    backLink: "/",
  };

  const handleOpenForm = (employee?: ColaboradorResponseDTO) => {
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

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
          <div className="flex flex-col items-start">
            <div className="mb-3">
              <h3 className="text-2xl font-bold text-base-content">Indicadores dos colaboradores</h3>
            </div>

            <div className="flex w-full justify-between">
              <KpiCard
                label="Ativos atualmente"
                value={colaboradoresQuery.data?.totalColaboradoresAtivos ?? 0}
                Icon={UserCheck}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />
              <KpiCard
                label="Total Desde o início"
                value={colaboradoresQuery.data?.totalColaboradoresAtivos ?? 0}
                Icon={UserCircle}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />
              <KpiCard
                label="Pago desde o Início"
                value={colaboradoresQuery.data?.totalColaboradoresAtivos ?? 0}
                Icon={CheckCircle}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />
            </div>
          </div>
        </section>

        <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="flex flex-col gap-1 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Colaboradores cadastrados</h3>
              <p className="text-sm text-base-content/60">
                Clique na linha para abrir os detalhes do cadastro.
              </p>
            </div>

            <Button className="sm:ml-auto" onClick={() => handleOpenForm()} variant="success">
              <Plus className="mr-2 h-4 w-4" />
              Novo Colaborador
            </Button>
          </div>

          <EmployeesTable/>
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
