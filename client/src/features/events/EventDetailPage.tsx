import type { ReactNode } from "react";
import { Calendar, UserCog } from "lucide-react";
import { useParams } from "react-router-dom";
import { ButtonLink } from "@/components/ui/button";
import { EmptyCard } from "@/components/ui/empty-card";
import { ErrorCard } from "@/components/ui/error-card";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import styles from "@/features/events/EventDetailPage.module.css";
import { eventContentLabels } from "@/features/events/schemas/eventContentEnum";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { useEventDetailQuery } from "./query/useEventQueries";
import { EditEventButton } from "./components/EditEventButton";
import { DeleteEventButton } from "./components/DeleteEventButton";

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";

  const {
    data: event,
    isLoading: isEventLoading,
    error: eventError,
  } = useEventDetailQuery(eventId);

  if (isEventLoading) {
    return <PageLoading message="Carregando evento..." />;
  }

  if (eventError) {
    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(eventError)} />
      </div>
    );
  }

  if (!event) {
    return (
      <div className={styles.page}>
        <EmptyCard
          title="Evento não encontrado"
          description="Não encontramos os dados deste evento."
        />
      </div>
    );
  }

  const profit = Number(event.price) - Number(event.payment);

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "ID", value: String(event.id) },
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
    <div className={styles.page}>
      <PageHeader
        description="Veja e gerencie as informações do evento"
        Icon={Calendar}
        title="Detalhes do evento"
        action={
          <ButtonLink to="/events" variant="outline">
            Voltar para eventos
          </ButtonLink>
        }
      />

      <SectionCard
        headerAction={<EditEventButton eventId={eventId} />}
        title="Resumo do evento"
        description="Dados completos do atendimento, participantes e valores."
      >
        <div className={styles.summaryGrid}>
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
