import { PageLayout } from "@/components/layout/PageLayout";
import { Handshake } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";

import { SectionCard } from "@/components/ui/section-card";
import { useGetParentById } from "@/kubb";
import { ParentForm } from "../components/ParentForm";
import { ParentInfoSection } from "../components/ParentInfoSection";
import { ParentStudentsTable } from "../components/ParentStudentsTable";

export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const parentQuery = useGetParentById(parentId);

  const headerProps = {
    description: "Veja os dados do responsável, os vínculos ativos e o histórico arquivado com alunos.",
    title: "Detalhes do Responsável",
    Icon: Handshake,
    backLink: "/parents",
  };

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-4 animate-[fade-up_300ms_ease-out_both]">
        <div className="animate-[fade-up_360ms_ease-out_both]">
          <ParentInfoSection parentId={parentId} onEdit={() => setIsFormOpen(true)} />
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
          <ParentStudentsTable parentId={parentId} />
        </SectionCard>

        {isFormOpen && parentQuery.data && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl border border-base-300 bg-base-100 shadow-2xl">
              <h3 className="mb-1 text-lg font-bold">Editar Responsavel</h3>
              <p className="mb-4 text-sm text-base-content/60">
                Revise os dados de contato, cadastro e identificacao do responsavel.
              </p>
              <ParentForm
                initialData={parentQuery.data}
                onSuccess={() => setIsFormOpen(false)}
                onCancel={() => setIsFormOpen(false)}
              />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
