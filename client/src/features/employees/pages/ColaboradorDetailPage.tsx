import { PageLayout } from "@/components/layout/PageLayout";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import {
  useFindColaboradorById,
  useGetAtendimentosByColaborador,
} from "@/kubb";
import { Calendar, CircleDollarSign, Clock3, FileUser } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { ColaboradorEventsTable } from "../components/ColaboradorEventsTable";
import { ColaboradorInfoSection } from "../components/ColaboradorInfoSection";
import { ColaboradorForm } from "../components/ColaboradorForm";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";

const headerProps = {
  description: "Veja e gerencie as informações do colaborador",
  title: "Detalhes do colaborador",
  Icon: FileUser,
  backLink: "/colaboradores",
};

export function ColaboradorDetailPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [hidePaid, setHidePaid] = useState(false);

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const colaboradorQuery = useFindColaboradorById(employeeId);

  const colaboradorAtendimentos = useGetAtendimentosByColaborador(employeeId, {
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
        <ColaboradorInfoSection colaboradorId={employeeId} onEdit={() => setIsFormOpen(true)} />
      </div>

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <KpiCard
            label="Total de Atendimentos"
            value={colaboradorAtendimentos.data?.summary?.totalAtendimentos}
            Icon={Calendar}
          />

          <KpiCard
            label="Total pago"
            value={<span className="text-success">{brl.format(colaboradorAtendimentos.data?.summary?.totalPaid ?? 0)}</span>}
            Icon={CircleDollarSign}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Total pendente"
            value={<span className="text-warning">{brl.format(colaboradorAtendimentos.data?.summary?.totalUnpaid ?? 0)}</span>}
            Icon={Clock3}
            className="bg-linear-to-br from-warning/10 via-base-100 to-base-100"
          />
        </div>
      </div>

      <div className="animate-[fade-up_600ms_ease-out_both]">
        <ColaboradorEventsTable
          atendimentos={colaboradorAtendimentos.data?.atendimentos}
          error={colaboradorAtendimentos.error}
          hidePaid={hidePaid}
          isLoading={colaboradorAtendimentos.isLoading}
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
            <ColaboradorForm
              initialData={colaboradorQuery.data}
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
