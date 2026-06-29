import { AtendimentosTableHeader } from "@/components/Atendimento/AtendimentosTable.Header.tsx";
import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { KpiCard } from "@/components/Ui/KpiCard.tsx";
import { BellElectric } from "lucide-react";
import { AtendimentosTable } from "../../components/Atendimento/AtendimentosTable.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import { Suspense, useState } from "react";
import { AtendimentoForm } from "../../components/Atendimento/AtendimentoForm.tsx";
import { ChartPie, NotebookPen } from "lucide-react";
import type {
  AtendimentoResponseStatusEnumKey,
  AtendimentoResponseTipoEnumKey,
} from "@/kubb";

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

  return (
    <PageLayout {...headerProps}>
      <section className="flex flex-col gap-3 p-4 rounded-2xl border border-base-300 bg-base-100 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <AtendimentosTableHeader
          openForm={() => setIsFormOpen(true)}
          filterStatus={filterStatus}
          filterTipo={filterTipo}
          onSearchChange={setSearch}
          onStatusChange={setFilterStatus}
          onTipoChange={setFilterTipo}
          title="Atendimentos"
          description="Navegue por mês e selecione um atendimento para visualizar os detalhes."
        />

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
        <Suspense
          fallback={
            <p className="text-sm text-base-content/60">
              Carregando formulário...
            </p>
          }
        >
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
