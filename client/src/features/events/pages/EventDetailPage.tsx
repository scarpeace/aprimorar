import { ButtonLink } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetEventById } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, Edit } from "lucide-react";
import { type ReactNode } from "react";
import { useParams } from "react-router-dom";

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const eventQuery = useGetEventById(eventId);

  const headerProps = {
    description: "Veja e gerencie as informações do evento",
    Icon: Calendar,
    title: "Detalhes do evento",
    backLink: "/events",
  };

  if (eventQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard title="Erro ao carregar evento" error={eventQuery.error} />
      </PageLayout>
    );
  }

  if (eventQuery.isPending || !eventQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do evento" />
      </PageLayout>
    );
  }

  const profit = Number(eventQuery.data.price) - Number(eventQuery.data.payment);

  const summaryItems: Array<{ label: string; value: ReactNode; className?: string }> = [
    { label: "Data", value: formatDateShortYear(eventQuery.data.startDate) },
    { label: "Hora", value: formatTime(eventQuery.data.startDate) },
    { label: "Duração", value: "1 hora" },
    { label: "Nome do aluno", value: eventQuery.data.studentName ?? "-" },
    { label: "Nome do colaborador", value: eventQuery.data.employeeName ?? "-" },
    { label: "Conteúdo", value: eventQuery.data.content ?? "-" },
    { label: "Valor", value: brl.format(eventQuery.data.price) },
    { label: "Pagamento", value: brl.format(eventQuery.data.payment) },
    { label: "Lucro", value: brl.format(profit) },
    { label: "Título", value: eventQuery.data.title },
    {
      label: "Descrição",
      value: eventQuery.data.description ?? "-",
      className: "col-span-2",
    },
  ];

  return (
    <PageLayout {...headerProps}>
      <SectionCard
        title="Resumo do evento"
        description="Dados completos do atendimento, participantes e valores."
        headerActions={
          <>
            <ButtonLink to={`/events/edit/${eventId}`} variant="primary">
              <Edit className="h-4 w-4" />
              Editar
            </ButtonLink>
          </>
        }
      >
        <div className="grid grid-cols-1 grid-rows-3 md:grid-cols-3 gap-4">

          {summaryItems.map((item) => (
            <SummaryItem
              key={item.label}
              className={item.className}
              label={item.label}
              value={item.value}
            />
          ))}
        </div>
      </SectionCard>
    </PageLayout>
  );
}
