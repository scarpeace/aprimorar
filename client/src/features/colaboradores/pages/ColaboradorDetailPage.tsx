import { PageLayout } from "@/components/layout/PageLayout";
import { Modal } from "@/components/ui/modal";
import { PageDateFilterWidget } from "@/components/layout/PageDateFilterWidget";
import {
  useFindColaboradorById,
  useGetAtendimentosByColaborador,
} from "@/kubb";
import { Calendar, CircleDollarSign, Clock3, FileUser } from "lucide-react";
import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { KpiCard } from "@/components/ui/kpi-card";
import { brl } from "@/lib/utils/formatter";
import { ColaboradorEventsTable } from "../components/ColaboradorEventsTable";
import { ColaboradorInfoSection } from "../components/ColaboradorInfoSection";
import { ColaboradorForm } from "../components/ColaboradorForm";
import { usePageDateFilter } from "@/lib/hooks/use-page-date-filter.ts";

export function ColaboradorDetailPage() {
  const { id } = useParams<{ id: string }>();
  const colaboradorId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [hidePaid, setHidePaid] = useState(false);
  const navigate = useNavigate();

  const dateFilter = usePageDateFilter();
  const { startDate, endDate } = dateFilter;

  const colaboradorQuery = useFindColaboradorById(colaboradorId);

  const colaboradorAtendimentos = useGetAtendimentosByColaborador(colaboradorId, {
    page: currentPage,
    sort: ["fim,desc", "id,asc"],
    inicio: startDate?.toISOString(),
    fim: endDate?.toISOString(),
    ocultarPagos: hidePaid,
  });

  const handleToggleHidePaid = (value: boolean) => {
    setHidePaid(value);
    setCurrentPage(0);
  };

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

      <div className="mb-3 animate-[fade-up_600ms_ease-out_both]">
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <KpiCard
            label="Total de Atendimentos"
            value={colaboradorAtendimentos.data?.resumo?.totalAtendimentos}
            Icon={Calendar}
          />

          <KpiCard
            label="Total pago"
            value={<span className="text-success">{brl.format(colaboradorAtendimentos.data?.resumo?.totalPago ?? 0)}</span>}
            Icon={CircleDollarSign}
            className="bg-linear-to-br from-success/8 via-base-100 to-base-100"
          />

          <KpiCard
            label="Total pendente"
            value={<span className="text-warning">{brl.format(colaboradorAtendimentos.data?.resumo?.totalPendente ?? 0)}</span>}
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

      <PageDateFilterWidget {...dateFilter} />
    </PageLayout>
  );
}
