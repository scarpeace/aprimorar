import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { useGetColaboradoresKpis, type ColaboradorResponseDTO } from "@/kubb";
import { CheckCircle, FileUser, Plus, UserCheck, UserCircle } from "lucide-react";
import { useState } from "react";
import { EmployeesTable } from "../components/EmployeesTable";
import { EmployeeForm } from "../components/EmployeeForm";
import { KpiCard } from "@/components/ui/kpi-card";

export function EmployeesPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState<ColaboradorResponseDTO | null>(null);

  const headerProps = {
    description: "Gerencie cadastros e matrículas.",
    title: "Colaboradores",
    Icon: FileUser,
    backLink: "/",
  };

  const { data: kpisColaboradores } = useGetColaboradoresKpis();
  const handleOpenForm = (employee?: ColaboradorResponseDTO) => {
    setSelectedEmployee(employee || null);
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setSelectedEmployee(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-3">
        <section className="rounded-2xl bg-base-100 p-4 shadow-sm animate-[fade-up_180ms_ease-out_both]">
          <div className="flex flex-row justify-between items-center gap-3">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Resumo dos Colaboradores</h3>
              <p className="text-sm text-base-content/60">
                Visão geral dos colaboradores ativos e do total cadastrado desde o inicio.
              </p>
            </div>

            <div className="flex gap-3">
              <KpiCard
                label="Ativos atualmente"
                value={kpisColaboradores?.totalColaboradoresAtivos}
                Icon={UserCheck}
                className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
              />
              <KpiCard
                label="Total Desde o início"
                value={kpisColaboradores?.totalColaboradores}
                Icon={UserCircle}
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
