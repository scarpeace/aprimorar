import { PageLayout } from "@/components/layout/PageLayout";
import { Modal } from "@/components/ui/modal";
import { Handshake } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";

import { SectionCard } from "@/components/ui/section-card";
import { ResponsavelForm } from "../components/ResponsavelForm";
import { ResponsavelInfoSection } from "../components/ResponsavelInfoSection";
import { ResponsavelAlunosTable } from "../components/ResponsavelAlunosTable";
import { useGetResponsavelById } from "@/kubb";

export function ResponsavelDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const responsavelQuery = useGetResponsavelById(parentId);

  const headerProps = {
    description: "Veja os dados do responsável, os vínculos ativos e o histórico arquivado com alunos.",
    title: "Detalhes do Responsável",
    Icon: Handshake,
    backLink: "/responsavels",
    iconBg: "info",
  } as const;

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-4 animate-[fade-up_300ms_ease-out_both]">
        <div className="animate-[fade-up_360ms_ease-out_both]">
          <ResponsavelInfoSection parentId={parentId} onEdit={() => setIsFormOpen(true)} />
        </div>

        <SectionCard
          title="Alunos vinculados"
          description="Relação de alunos ativos e histórico arquivado associados a este responsável."
          headerActions={
            <div className="rounded-full border border-primary/15 bg-primary/8 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-primary">
              Historico familiar
            </div>
          }
        >
          <ResponsavelAlunosTable parentId={parentId} />
        </SectionCard>
      </div>
      <Modal
        isOpen={isFormOpen && !!responsavelQuery.data}
        onClose={() => setIsFormOpen(false)}
        title="Editar Responsavel"
        description="Revise os dados de contato, cadastro e identificacao do responsavel."
        size="md"
      >
        <ResponsavelForm
          initialData={responsavelQuery.data}
          onSuccess={() => setIsFormOpen(false)}
          onCancel={() => setIsFormOpen(false)}
        />
      </Modal>
    </PageLayout>
  );
}
