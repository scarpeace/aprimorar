import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import {
  useFindColaboradorById,
  type AtendimentoResponseStatusEnumKey,
  type AtendimentoResponseTipoEnumKey,
} from "@/kubb";
import { FileUser } from "lucide-react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ColaboradorInfoSection } from "../../components/Colaborador/ColaboradorInfoSection.tsx";
import { ColaboradorForm } from "../../components/Colaborador/ColaboradorForm.tsx";
import { AtendimentosTable } from "@/components/Atendimento/AtendimentosTable.tsx";
import { AtendimentosTableHeader } from "@/components/Atendimento/AtendimentosTable.Header.tsx";

export function ColaboradorDetailPage() {
  const { id } = useParams<{ id: string }>();
  const colaboradorId = id ?? "";
  const navigate = useNavigate();

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [search, setSearch] = useState("");
  const [filterStatus, setFilterStatus] = useState<AtendimentoResponseStatusEnumKey | "">("");
  const [filterTipo, setFilterTipo] = useState<AtendimentoResponseTipoEnumKey | "">("");

  const colaboradorQuery = useFindColaboradorById(colaboradorId);

  const headerProps = {
    description: "Veja e gerencie as informações do colaborador",
    title: "Detalhes do colaborador",
    Icon: FileUser,
    iconBg: "accent",
  } as const;

  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <ColaboradorInfoSection colaboradorId={colaboradorId} onEdit={() => setIsFormOpen(true)} />
      </div>

      <section className="flex flex-col gap-3 p-4 rounded-2xl border border-base-300 bg-base-100 shadow-sm animate-[fade-up_320ms_ease-out_both]">
        <AtendimentosTableHeader
          openForm={() => setIsFormOpen(true)}
          filterStatus={filterStatus}
          filterTipo={filterTipo}
          onSearchChange={setSearch}
          onStatusChange={setFilterStatus}
          onTipoChange={setFilterTipo}
          title="Eventos Vinculados"
          description="Veja e navegue pelos eventos vinculados ao colaborador."
        />

        <AtendimentosTable
          colaboradorId={colaboradorId}
          search={search}
          filterStatus={filterStatus}
          filterTipo={filterTipo}
        />
      </section>

      <Modal
        isOpen={isFormOpen}
        onClose={() => setIsFormOpen(false)}
        title="Editar Colaborador"
          description="Atualize dados pessoais, contato e função do colaborador para manter a operação organizada."
        size="md"
      >
        <ColaboradorForm
          initialData={colaboradorQuery.data}
          onSuccess={() => {
            setIsFormOpen(false);
            navigate(`/colaboradores/${colaboradorId}`);
          }}
          onCancel={() => setIsFormOpen(false)}
        />
      </Modal>

    </PageLayout>
  );
}
