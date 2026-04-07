import { ButtonLink } from "@/components/ui/button";
import { ComponentState } from "@/components/ui/component-state";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetEventById } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, Edit } from "lucide-react";
import { useParams } from "react-router-dom";

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const {
    data: event,
    isPending: isEventPending,
    isError: isEventError,
    error: eventError,
  } = useGetEventById(eventId);

  if (isEventPending || isEventError) {
    return <ComponentState isPending={isEventPending} error={eventError} />;
  }

  const profit = Number(event.price) - Number(event.payment);

  if (isEventError) {
    return <ErrorCard title="Erro ao carregar evento" error={eventError} />;
  }

  if (isEventPending) {
    return <LoadingCard title="Carregando dados do evento" />;
  }

  return (
    <div className="flex flex-col">
      <PageHeader
        description="Veja e gerencie as informações do evento"
        Icon={Calendar}
        title="Detalhes do evento"
      />

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
        {/*TODO: aplicar esse padrão para todos os detalhes das entidades*/}
        <div className="grid grid-cols-1 grid-rows-3 md:grid-cols-3 gap-4">

          <SummaryItem key={event.startDate} label="Data" value={formatDateShortYear(event.startDate)} />
          <SummaryItem key={event.startDate} label="Hora" value={formatTime(event.startDate)} />
          <SummaryItem key={"duracao"} label="Duração" value={"1 hora"} />

          <SummaryItem key={event.studentName} label="Nome do aluno" value={event.studentName ?? "-"} />
          <SummaryItem key={event.employeeName} label="Nome do colaborador" value={event.employeeName ?? "-"} />
          <SummaryItem key={event.content} label="Conteúdo" value={event.content ?? "-"} />

          <SummaryItem key={event.price} label="Valor" value={brl.format(event.price)} />
          <SummaryItem key={event.payment} label="Pagamento" value={brl.format(event.payment)} />
          <SummaryItem key={profit} label="Lucro" value={brl.format(profit)} />

          <SummaryItem key={event.title} label="Título" value={event.title} />
          <SummaryItem className="col-span-2" key={event.description} label="Descrição" value={event.description ?? "-"} />

        </div>
      </SectionCard>
    </div>
  );
}
