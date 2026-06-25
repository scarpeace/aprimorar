import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import {
  useFindColaboradorById,
  useGetAtendimentos,
} from "@/kubb";
import { FileUser } from "lucide-react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ColaboradorEventsTable } from "../../components/Colaborador/ColaboradorEventsTable.tsx";
import { ColaboradorInfoSection } from "../../components/Colaborador/ColaboradorInfoSection.tsx";
import { ColaboradorForm } from "../../components/Colaborador/ColaboradorForm.tsx";
import { usePageDateFilter } from "@/hooks/usePageDateFilter.ts";

export function ColaboradorDetailPage() {
  const { id } = useParams<{ id: string }>();
  const colaboradorId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const navigate = useNavigate();

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const colaboradorQuery = useFindColaboradorById(colaboradorId);

  const colaboradorAtendimentos = useGetAtendimentos({
    colaboradorId,
    page: currentPage,
    sort: ["fim,desc", "id,asc"],
    inicio: startDate?.toISOString(),
    fim: endDate?.toISOString(),
  });

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

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <ColaboradorEventsTable
          atendimentos={colaboradorAtendimentos.data}
          error={colaboradorAtendimentos.error}
          isLoading={colaboradorAtendimentos.isLoading}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </div>

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
