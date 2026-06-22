import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { Modal } from "@/components/modal.tsx";
import { Handshake } from "lucide-react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { ResponsavelForm } from "../../components/Responsavel/ResponsavelForm.tsx";
import { ResponsavelInfoSection } from "../../components/Responsavel/ResponsavelInfoSection.tsx";
import { ResponsavelAlunosTable } from "../../components/Responsavel/ResponsavelAlunosTable.tsx";
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

        {/*<div*/}
        {/*  title="Alunos vinculados"*/}
        {/*  description="Relação de alunos associados a este responsável."*/}
        {/*  headerActions={*/}
        {/*    <div className="rounded-full border border-primary/15 bg-primary/8 px-3 py-1 text-xs font-semibold uppercase tracking-wide text-primary">*/}
        {/*      Histórico familiar*/}
        {/*    </div>*/}
        {/*  }*/}
        {/*>*/}
          <ResponsavelAlunosTable responsavelId={responsavelId} />
        {/*</div>*/}
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
