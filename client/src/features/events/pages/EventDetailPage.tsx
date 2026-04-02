import { ErrorCard } from "@/components/ui/error-card";
import { LoadingSpinner } from "@/components/ui/loading-spinner";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { eventContentLabels } from "@/features/events/hooks/eventContentLabels";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar } from "lucide-react";
import type { ReactNode } from "react";
import { useParams } from "react-router-dom";
import { EditEventButton } from "../components/EditEventButton";
import { useEventById } from "../hooks/use-event-queries";
import { ComponentState } from "@/components/ui/component-state";

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const {
    data: event,
    isPending: isEventPending,
    isError: isEventError,
    error: eventError,
  } = useEventById(eventId);

  if(isEventPending || isEventError) {
    return <ComponentState isPending={isEventPending} error={eventError} />;
  }

  const profit = Number(event.price) - Number(event.payment);

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "ID", value: String(event.eventId) },
    { label: "Título", value: event.title },
    { label: "Descrição", value: event.description ?? "-" },
    { label: "Conteúdo", value: eventContentLabels[event.content] },
    { label: "Aluno", value: event.studentName },
    { label: "Colaborador", value: event.employeeName },
    { label: "Data", value: formatDateShortYear(event.startDate) },
    {
      label: "Horário",
      value: `${formatTime(event.startDate)} : ${formatTime(event.endDate)}`,
    },
    { label: "Preço", value: brl.format(event.price) },
    { label: "Pagamento (custo)", value: brl.format(event.payment) },
    { label: "Lucro", value: brl.format(profit) },
    { label: "Criado em", value: formatDateShortYear(event.createdAt) },
  ];

  return (
    <div className="flex flex-col gap-7">
      <PageHeader
        description="Veja e gerencie as informações do evento"
        Icon={Calendar}
        title="Detalhes do evento"
      />

      <SectionCard
        headerAction={<EditEventButton eventId={eventId} />}
        title="Resumo do evento"
        description="Dados completos do atendimento, participantes e valores."
      >
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {summaryItems.map((item) => (
            <SummaryItem
              key={item.label}
              label={item.label}
              value={item.value}
            />
          ))}
        </div>
      </SectionCard>
    </div>
  );
}
