import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import {
  useBuscarAlunoPorId,
  useGetAtendimentosByAluno,
} from "@/kubb";
import {
  Calendar,
  CircleDollarSign,
  Clock3,
  GraduationCap,
} from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { AlunoEventsTable } from "../components/AlunoEventsTable";
import { AlunoInfoSection } from "../components/AlunoInfoSection";
import { AlunoForm } from "../components/AlunoForm";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";

const headerProps = {
  description: "Veja e gerencie as informações do aluno",
  title: "Detalhes do aluno",
  Icon: GraduationCap,
  backLink: "/alunos",
};

export function AlunoDetailPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [hideCharged, setHideCharged] = useState(false);

  const alunoQuery = useBuscarAlunoPorId(studentId);

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const alunoAppointments = useGetAtendimentosByAluno(studentId, {
    page: currentPage,
    sort: ["endDate,desc", "id,asc"],
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    charged: hideCharged ? false : undefined,
  });

  const handleToggleHideCharged = (value: boolean) => {
    setHideCharged(value);
    setCurrentPage(0);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <AlunoInfoSection
          studentId={studentId}
          onEdit={() => setIsFormOpen(true)}
        />
      </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <KpiCard
            label="Total de Atendimentos"
            value={alunoAppointments.data?.summary?.totalAtendimentos}
            Icon={Calendar}
          />

          <KpiCard
            label="Total pago"
            value={<span className="text-success">{brl.format(alunoAppointments.data?.summary?.totalCharged ?? 0)}</span>}
            Icon={CircleDollarSign}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Total pendente"
            value={<span className="text-warning">{brl.format(alunoAppointments.data?.summary?.totalPending ?? 0)}</span>}
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

      {isFormOpen && (
        <div className="modal modal-open">
          <div className="modal-box max-w-4xl overflow-hidden">
            <h3 className="font-bold text-lg mb-4">Editar Aluno</h3>
            <AlunoForm
              initialData={alunoQuery.data}
              onSuccess={() => setIsFormOpen(false)}
              onCancel={() => setIsFormOpen(false)}
            />
          </div>
        </div>
      )}

      <PageDateFilterWidget {...dateFilter} />
    </PageLayout>
  );
}
