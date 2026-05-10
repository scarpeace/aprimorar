import { PageLayout } from "@/components/layout/PageLayout";
import { Handshake } from "lucide-react";
import { useParams } from "react-router-dom";

import { SectionCard } from "@/components/ui/section-card";
import { useGetParentById } from "@/kubb";
import { useState } from "react";
import { ParentForm } from "../components/ParentForm";
import { ParentInfoSection } from "../components/ParentInfoSection";
import { ParentStudentsTable } from "../components/ParentStudentsTable";

export function ParentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const parentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);
  const [isFormOpen, setIsFormOpen] = useState(false);

  const parentQuery = useGetParentById(parentId)

  const headerProps = {
    description: "Veja os dados do responsável, os vínculos ativos e o histórico arquivado com alunos.",
    title: "Detalhes do Responsável",
    Icon: Handshake,
    backLink: "/parents",
  };

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <ParentInfoSection parentId={parentId} onEdit={() => setIsFormOpen(true)} />

        <SectionCard
          title="Alunos vinculados"
          description="Relação de alunos ativos e histórico arquivado associados a este responsável."
        >
          <ParentStudentsTable
            parentId={parentId}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
        </SectionCard>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl">
              <h3 className="font-bold text-lg mb-4">Editar Responsável</h3>
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
