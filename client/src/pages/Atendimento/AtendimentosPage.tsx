import { AtendimentosTableFilters } from "@/components/Atendimento/AtendimentosTable.Filters.tsx";
import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { KpiCard } from "@/components/Ui/KpiCard.tsx";
import { BellElectric } from "lucide-react";
import { AtendimentosTable } from "../../components/Atendimento/AtendimentosTable.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import { Suspense, useState } from "react";
import { AtendimentoForm } from "../../components/Atendimento/AtendimentoForm.tsx";
import { ChartPie, NotebookPen } from "lucide-react";
import type { AtendimentoResponseStatusEnumKey, AtendimentoResponseTipoEnumKey } from "@/kubb";

export function AtendimentosPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [search, setSearch] = useState("");
  const [filterStatus, setFilterStatus] = useState<AtendimentoResponseStatusEnumKey | "">("");
  const [filterTipo, setFilterTipo] = useState<AtendimentoResponseTipoEnumKey | "">("");

  const headerProps = {
    description: "Gerencie aulas e Atendimento.",
    title: "Atendimento",
    Icon: BellElectric,
    iconBg: "accent",
  } as const;
  const mockIndicators = [
    {
      label: "Total atendimentos do mês",
      value: 42,
      Icon: NotebookPen,
    },
    {
      label: "Distribuição do Tipo",
      value: "Mock",
      Icon: ChartPie,
    },
  ] as const;

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <div className="flex flex-col">
          <div className="flex flex-col lg:flex-row lg:items-start lg:justify-between">
            <div>
              <h3 className="text-2xl font-bold text-base-content">Atendimentos</h3>
              <p className="text-sm text-base-content/60">
                Navegue por mês e selecione um atendimento para visualizar os detalhes.
              </p>
            </div>

            <div className="flex gap-3">
              {mockIndicators.map(({ label, value, Icon }) => (
                <KpiCard
                  key={label}
                  label={label}
                  value={value}
                  Icon={Icon}
                  className="min-w-52 bg-linear-to-br from-accent/8 via-base-100 to-base-100"
                />
              ))}
            </div>
          </div>

          <AtendimentosTableFilters
            openForm={() => setIsFormOpen(true)}
            filterStatus={filterStatus}
            filterTipo={filterTipo}
            onSearchChange={setSearch}
            onStatusChange={setFilterStatus}
            onTipoChange={setFilterTipo}
          />
        </div>
      </section>

      <section className="rounded-2xl mt-3 border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <AtendimentosTable
          search={search}
          filterStatus={filterStatus}
          filterTipo={filterTipo}
        />
      </section>

      <Modal
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        title="Cadastrar Novo Atendimento"
        description="Defina aluno, colaborador, horario e valores do atendimento para manter agenda e financeiro sincronizados."
        size="lg"
      >
        <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
          <AtendimentoForm
            initialData={null}
            onSuccess={() => setIsFormOpen(false)}
            onCancel={() => setIsFormOpen(false)}
          />
        </Suspense>
      </Modal>

    </PageLayout>
  );
}
