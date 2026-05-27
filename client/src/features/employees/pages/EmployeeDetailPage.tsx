import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import {
  useFindColaboradorById,
  useGetAtendimentosByColaborador,
} from "@/kubb";
import { CircleDollarSign, Clock3, FileUser } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { EmployeeEventsTable } from "../components/EmployeeEventsTable";
import { EmployeeInfoSection } from "../components/EmployeeInfoSection";
import { EmployeeForm } from "../components/EmployeeForm";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";

const headerProps = {
  description: "Veja e gerencie as informações do colaborador",
  title: "Detalhes do colaborador",
  Icon: FileUser,
  backLink: "/employees",
};

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [hidePaid, setHidePaid] = useState(false);

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const employeeQuery = useFindColaboradorById(employeeId);

  const employeeAppointments = useGetAtendimentosByColaborador(employeeId, {
    page: currentPage,
    sort: ["endDate,desc", "id,asc"],
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
    hidePaid,
  });

  const handleToggleHidePaid = (value: boolean) => {
    setHidePaid(value);
    setCurrentPage(0);
  };

  return (
    <PageLayout {...headerProps}>
      <div className="mb-3">
        <EmployeeInfoSection employeeId={employeeId} onEdit={() => setIsFormOpen(true)} />
      </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-2">
          <KpiCard
            label="Total pago"
            value={<span className="text-success">{brl.format(employeeAppointments.data?.summary?.totalPaid ?? 0)}</span>}
            Icon={CircleDollarSign}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Total pendente"
            value={<span className="text-warning">{brl.format(employeeAppointments.data?.summary?.totalUnpaid ?? 0)}</span>}
            Icon={Clock3}
            className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
          />
        </div>
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <EmployeeEventsTable
          appointments={employeeAppointments.data?.atendimentos}
          error={employeeAppointments.error}
          hidePaid={hidePaid}
          isLoading={employeeAppointments.isLoading}
          currentPage={currentPage}
          onHidePaidChange={handleToggleHidePaid}
          onPageChange={setCurrentPage}
        />
      </div>

      {isFormOpen && (
        <div className="modal modal-open">
          <div className="modal-box max-w-2xl border border-base-300 bg-base-100 shadow-2xl">
            <h3 className="mb-1 text-lg font-bold">Editar Colaborador</h3>
            <p className="mb-4 text-sm text-base-content/60">
              Atualize dados pessoais, contato e funcao do colaborador para manter a operacao organizada.
            </p>
            <EmployeeForm
              initialData={employeeQuery.data}
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
