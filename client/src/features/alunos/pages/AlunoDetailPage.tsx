import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import { KpiCard } from "@/components/ui/kpi-card";
import { Modal } from "@/components/ui/modal";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";
import { brl } from "@/lib/utils/formatter";
import {
  useGetAlunoById,
  useGetAtendimentosByAluno,
} from "@/kubb";
import {
  Calendar,
  CircleDollarSign,
  Clock3,
  GraduationCap,
} from "lucide-react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { AlunoEventsTable } from "../components/AlunoEventsTable";
import { AlunoForm } from "../components/AlunoForm";
import { AlunoInfoSection } from "../components/AlunoInfoSection";

export function AlunoDetailPage() {
  const { id } = useParams<{ id: string }>();
  const alunoId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [hideCharged, setHideCharged] = useState(false);
  const navigate = useNavigate();

  const alunoQuery = useGetAlunoById(alunoId);

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const alunoAppointments = useGetAtendimentosByAluno(alunoId, {
    page: currentPage,
    sort: ["fim,desc", "id,asc"],
    inicio: startDate?.toISOString(),
    fim: endDate?.toISOString(),
    cobrado: hideCharged ? false : undefined,
  });

  const handleToggleHideCharged = (value: boolean) => {
    setHideCharged(value);
    setCurrentPage(0);
  };

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

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <KpiCard
            label="Total de Atendimentos"
            value={alunoAppointments.data?.resumo?.totalAtendimentos}
            Icon={Calendar}
          />

          <KpiCard
            label="Total pago"
            value={<span className="text-success">{brl.format(alunoAppointments.data?.resumo?.totalCobrado ?? 0)}</span>}
            Icon={CircleDollarSign}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Total pendente"
            value={<span className="text-warning">{brl.format(alunoAppointments.data?.resumo?.totalPendente ?? 0)}</span>}
            Icon={Clock3}
            className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
          />
        </div>
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <AlunoEventsTable
          atendimentos={alunoAppointments.data?.atendimentos}
          error={alunoAppointments.error}
          hideCharged={hideCharged}
          isLoading={alunoAppointments.isLoading}
          currentPage={currentPage}
          onHideChargedChange={handleToggleHideCharged}
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

      <PageDateFilterWidget {...dateFilter} />
    </PageLayout>
  );
}
