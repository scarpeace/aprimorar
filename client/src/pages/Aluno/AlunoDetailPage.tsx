import { PageLayout } from "@/components/Layout/PageLayout.tsx";
import { Modal } from "@/components/Ui/Modal.tsx";
import { usePageDateFilter } from "@/hooks/usePageDateFilter.ts";
import {
  useGetAlunoById,
  useGetAtendimentos,
} from "@/kubb";
import {
  GraduationCap,
} from "lucide-react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { AtendimentosAlunoTable } from "../../components/Aluno/AtendimentosAlunoTable.tsx";
import { AlunoForm } from "../../components/Aluno/AlunoForm.tsx";
import { AlunoInfoSection } from "../../components/Aluno/AlunoInfoSection.tsx";

export function AlunoDetailPage() {
  const { id } = useParams<{ id: string }>();
  const alunoId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const navigate = useNavigate();

  const alunoQuery = useGetAlunoById(alunoId);

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const alunoAppointments = useGetAtendimentos({
    alunoId,
    page: currentPage,
    sort: ["fim,desc", "id,asc"],
    inicio: startDate?.toISOString(),
    fim: endDate?.toISOString(),
  });

  const handleOpenForm = () => {
    setIsFormOpen(true);
  };

  const handleCloseForm = () => {
    setIsFormOpen(false);
  };

  const headerProps = {
    description: "Veja e gerencie as informações do aluno",
    title: "Detalhes do aluno",
    Icon: GraduationCap,
    iconBg: "success",
  } as const;

  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <AlunoInfoSection
          alunoId={alunoId}
          onEdit={handleOpenForm}
        />
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <AtendimentosAlunoTable
          atendimentos={alunoAppointments.data}
          error={alunoAppointments.error}
          isLoading={alunoAppointments.isLoading}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </div>

      <Modal
        isOpen={isFormOpen}
        onClose={handleCloseForm}
        title="Editar Aluno"
        description="Atualize os dados do aluno para manter o cadastro consistente."
        size="lg"
      >
        <AlunoForm
          initialData={alunoQuery.data}
          onSuccess={() => {
            handleCloseForm();
            navigate(`/alunos/${alunoId}`);
          }}
          onCancel={handleCloseForm}
        />
      </Modal>

    </PageLayout>
  );
}
