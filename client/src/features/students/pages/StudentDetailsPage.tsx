import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import {
  useGetAtendimentosByStudentId,
  useBuscarAlunoPorId,
} from "@/kubb";
import {
  Calendar,
  CircleDollarSign,
  Clock3,
  GraduationCap,
} from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { StudentEventsTable } from "../components/StudentEventsTable";
import { StudentInfoSection } from "../components/StudentInfoSection";
import { StudentForm } from "../components/StudentForm";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";

const headerProps = {
  description: "Veja e gerencie as informações do aluno",
  title: "Detalhes do aluno",
  Icon: GraduationCap,
  backLink: "/students",
};

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [hideCharged, setHideCharged] = useState(false);

  const studentQuery = useBuscarAlunoPorId(studentId);

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const studentAppointments = useGetAtendimentosByStudentId(studentId, {
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
        <StudentInfoSection
          studentId={studentId}
          onEdit={() => setIsFormOpen(true)}
        />
      </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <KpiCard
            label="Total de eventos"
            value={studentAppointments.data?.summary?.totalEvents}
            Icon={Calendar}
          />

          <KpiCard
            label="Total pago"
            value={<span className="text-success">{brl.format(studentAppointments.data?.summary?.totalCharged ?? 0)}</span>}
            Icon={CircleDollarSign}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Total pendente"
            value={<span className="text-warning">{brl.format(studentAppointments.data?.summary?.totalPending ?? 0)}</span>}
            Icon={Clock3}
            className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
          />
        </div>
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <StudentEventsTable
          appointments={studentAppointments.data?.appointments}
          error={studentAppointments.error}
          hideCharged={hideCharged}
          isLoading={studentAppointments.isLoading}
          currentPage={currentPage}
          onHideChargedChange={handleToggleHideCharged}
          onPageChange={setCurrentPage}
        />
      </div>

      {isFormOpen && (
        <div className="modal modal-open">
          <div className="modal-box max-w-4xl overflow-hidden">
            <h3 className="font-bold text-lg mb-4">Editar Aluno</h3>
            <StudentForm
              initialData={studentQuery.data}
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
