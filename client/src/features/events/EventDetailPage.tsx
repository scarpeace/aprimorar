import type { ReactNode } from "react"
import { Calendar } from "lucide-react"
import { useParams } from "react-router-dom"
import { ButtonLink } from "@/components/ui/button"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import styles from "@/features/events/EventDetailPage.module.css"
import { eventContentLabels } from "@/lib/shared/enums"
import { brl, formatDateShortYear, formatTime } from "@/lib/shared/formatter"
import { getFriendlyErrorMessage } from "@/services/api"
import { useEventDetailQuery, useDeleteEvent } from "./hooks/use-events"
import { DetailsPageActions } from "@/components/ui/details-page-actions"

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>()
  const eventId = id ?? ""

  const { data: event, isLoading: isEventLoading, error: eventError } = useEventDetailQuery(eventId)

  const { mutate: deleteEvent, isPending: isDeletePending } = useDeleteEvent()

  const handleDelete = () => {
    if (!globalThis.confirm("Tem certeza que deseja excluir este evento? Esta ação não pode ser desfeita.")) return
    deleteEvent(eventId)
  }


  if (isEventLoading) {
    return <PageLoading message="Carregando evento..." />
  }

  if (eventError) {
    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(eventError)} />
      </div>
    )
  }

  if (!event) {
    return (
      <div className={styles.page}>
        <EmptyCard title="Evento não encontrado" description="Não encontramos os dados deste evento." />
      </div>
    )
  }

  const profit = Number(event.price) - Number(event.payment)

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "ID", value: String(event.id) },
    { label: "Título", value: event.title },
    { label: "Descrição", value: event.description ?? "-" },
    { label: "Conteúdo", value: eventContentLabels[event.content] },
    { label: "Aluno", value: event.studentName },
    { label: "Colaborador", value: event.employeeName },
    { label: "Data", value: formatDateShortYear(event.startDate) },
    { label: "Horário", value: `${formatTime(event.startDate)} : ${formatTime(event.endDate)}` },
    { label: "Preço", value: brl.format(event.price) },
    { label: "Pagamento (custo)", value: brl.format(event.payment) },
    { label: "Lucro", value: brl.format(profit) },
    { label: "Criado em", value: formatDateShortYear(event.createdAt) },
  ]

  return (
    <div className={styles.page}>
      <PageHeader
        action={
          <ButtonLink to="/events" variant="outline">
            Voltar para eventos
          </ButtonLink>
        }
        description="Veja e gerencie as informações do evento"
        leading={
          <div className="flex h-12 w-12 items-center justify-center rounded-full bg-accent/20">
            <Calendar className="h-6 w-6 text-accent" />
          </div>
        }
        title="Detalhes do evento"
        titleClassName="text-2xl font-bold app-text"
      />

      <SectionCard
        headerAction={
          <DetailsPageActions
            data={event}
            editTo={`/events/edit/${event.id}`}
            handleDelete={handleDelete}
            isDeletePending={isDeletePending}
          />
        }
        title="Resumo do evento"
        description="Dados completos do atendimento, participantes e valores."
      >
        <div className={styles.summaryGrid}>
          {summaryItems.map((item) => (
            <SummaryItem key={item.label} label={item.label} value={item.value} />
          ))}
        </div>
      </SectionCard>
    </div>
  )
}
