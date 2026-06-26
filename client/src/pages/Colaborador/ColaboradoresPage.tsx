import { ColaboradoresTableFilters } from "@/components/Colaborador/ColaboradoresTable.Filters.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { useGetColaboradoresKpis, type ColaboradorResponseDTO } from "@/kubb";
import { UserCheck, UserCircle, UserCog } from "lucide-react";
import { useState } from "react";
import { ColaboradoresTable } from "../../components/Colaborador/ColaboradoresTable.tsx";
import { ColaboradorForm } from "../../components/Colaborador/ColaboradorForm.tsx";
import { KpiCard } from "@/components/Ui/KpiCard.tsx";

export function ColaboradoresPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [selectedColaborador, setSelectedColaborador] = useState<ColaboradorResponseDTO | null>(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [showArchived, setShowArchived] = useState(false);

  const headerProps = {
    description: "Gerencie cadastros e funções dos colaboradores.",
    title: "Colaboradores",
    Icon: UserCog,
    iconBg: "warning",
  } as const;

  const { data: kpisColaboradores } = useGetColaboradoresKpis();
  const handleCloseForm = () => {
    setSelectedColaborador(null);
    setIsFormOpen(false);
  };

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <div className="flex flex-col">
            <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Colaboradores</h3>
              <p className="text-sm text-base-content/60">
                Visão geral dos colaboradores ativos e do total cadastrado desde o início.
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
          <ColaboradoresTableFilters
            openForm={() => setIsFormOpen(true)}
            showArchived={showArchived}
            onSearchChange={setSearchTerm}
            onShowArchivedChange={(value) => {
              setShowArchived(value);
            }}
          />
        </div>
        </section>

        <section className="rounded-2xl mt-3 border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
          <ColaboradoresTable
            key={`${showArchived}-${searchTerm}`}
            searchTerm={searchTerm}
            showArchived={showArchived}
          />
        </section>

        <Modal
          isOpen={isFormOpen}
          onClose={handleCloseForm}
          title={selectedColaborador ? "Editar Colaborador" : "Cadastrar Novo Colaborador"}
          description="Informe os dados do colaborador para seu cadastro ou edição."
          size="lg"
        >
          <ColaboradorForm
            initialData={selectedColaborador}
            onSuccess={handleCloseForm}
            onCancel={handleCloseForm}
          />
        </Modal>
    </PageLayout>
  );
}
