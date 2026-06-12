import { PageLayout } from "@/components/layout/PageLayout";
import { Modal } from "@/components/ui/modal";
import { Handshake } from "lucide-react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { SectionCard } from "@/components/ui/section-card";
import { ResponsavelForm } from "../components/ResponsavelForm";
import { ResponsavelInfoSection } from "../components/ResponsavelInfoSection";
import { ResponsavelAlunosTable } from "../components/ResponsavelAlunosTable";
import { useGetResponsavelById } from "@/kubb";

export function ResponsavelDetailPage() {
  const { id } = useParams<{ id: string }>();
  const responsavelId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const navigate = useNavigate();

  const responsavelQuery = useGetResponsavelById(responsavelId);

  const headerProps = {
    description: "Veja os dados do responsável e os alunos vinculados.",
    title: "Detalhes do Responsável",
    Icon: Handshake,
    iconBg: "info",
  } as const;

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-4 animate-[fade-up_300ms_ease-out_both]">
        <div className="animate-[fade-up_360ms_ease-out_both]">
          <ResponsavelInfoSection responsavelId={responsavelId} onEdit={() => setIsFormOpen(true)} />
        </div>

        <SectionCard
          title="Alunos vinculados"
          description="Relação de alunos associados a este responsável."
          headerActions={
            <div className="rounded-full border border-primary/15 bg-primary/8 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-primary">
              Histórico familiar
            </div>
          }
        >
          <ResponsavelAlunosTable responsavelId={responsavelId} />
        </SectionCard>
      </div>
      <Modal
        isOpen={isFormOpen && !!responsavelQuery.data}
        onClose={() => setIsFormOpen(false)}
        title="Editar Responsável"
        description="Revise os dados de contato, cadastro e identificação do responsável."
        size="md"
      >
        <ResponsavelForm
          initialData={responsavelQuery.data}
          onSuccess={() => {
            setIsFormOpen(false);
            navigate(`/responsaveis/${responsavelId}`);
          }}
          onCancel={() => setIsFormOpen(false)}
        />
      </Modal>
    </PageLayout>
  );
}
