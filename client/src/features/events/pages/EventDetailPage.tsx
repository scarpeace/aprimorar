import { PageLayout } from "@/components/layout/PageLayout";
import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetEventById } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, Edit } from "lucide-react";
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

  return (
    <PageLayout {...headerProps}>
      <SectionCard
        title="Resumo do evento"
        description="Dados completos do atendimento, participantes e valores."
        headerActions={
          <>
            <ButtonLink to={`/events/edit/${eventId}`} variant="primary">
              <Edit className="h-4 w-4" /> Editar
            </ButtonLink>
          </>
        }
      >
        <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
          <SummaryItem label="Data" value={formatDateShortYear(eventQuery.data.startDate)}/>
          <div className="flex gap-3">
            <SummaryItem className="w-full" label="Horário início" value={formatTime(eventQuery.data.startDate)} />
            {/*<SummaryItem label="Duração" value="1 hora" />*/}
            <SummaryItem className="w-full" label="Horário fim" value={formatTime(eventQuery.data.endDate)}/>
          </div>
          <SummaryItem label="Nome do aluno" value={eventQuery.data.studentName} />
          <SummaryItem label="Nome do colaborador" value={eventQuery.data.employeeName}/>
          <SummaryItem label="Conteúdo" value={eventQuery.data.content} />
          <div className="flex gap-3">
            <SummaryItem className="w-full" label="Valor" value={brl.format(eventQuery.data.price)} />
            <SummaryItem className="w-full" label="Pagamento" value={brl.format(eventQuery.data.payment)}/>
            <SummaryItem className="w-full" label="Lucro" value={brl.format(profit)} />
          </div>
          {/*<SummaryItem label="Título" value={eventQuery.data.title} />*/}
          <SummaryItem className="md:col-span-3 h-30" label="Observações" value={eventQuery.data.description}/>
      </div>
      </SectionCard>
    </PageLayout>
  );
}
