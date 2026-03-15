import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query"
import type { ReactNode } from "react"
import { Calendar } from "lucide-react"
import { useParams, useNavigate } from "react-router-dom"
import { Button, ButtonLink } from "@/components/ui/button"
import { EmptyCard } from "@/components/ui/empty-card"
import { ErrorCard } from "@/components/ui/error-card"
import { PageHeader } from "@/components/ui/page-header"
import { PageLoading } from "@/components/ui/page-loading"
import { SectionCard } from "@/components/ui/section-card"
import { SummaryItem } from "@/components/ui/summary-item"
import styles from "@/features/events/EventDetailPage.module.css"
import { queryKeys } from "@/lib/query/queryKeys"
import { eventContentLabels } from "@/lib/shared/enums"
import { brl, formatDateShortYear, formatTime } from "@/lib/shared/formatter"
import { eventsApi, getFriendlyErrorMessage } from "@/services/api"

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>()
  const eventId = id ?? ""
  const queryClient = useQueryClient()
  const navigate = useNavigate()

  const eventQuery = useQuery({
    queryKey: [...queryKeys.events, eventId],
    queryFn: () => eventsApi.getById(eventId),
    enabled: Boolean(id),
  })

  const deleteEventMutation = useMutation({
    mutationFn: () => eventsApi.delete(eventId),
    onSuccess: async () => {
      await queryClient.invalidateQueries({ queryKey: queryKeys.events })
      globalThis.alert("Evento excluído com sucesso")
      navigate("/events")
    },
  })

  if (!id) {
    return (
      <div className={styles.page}>
        <ErrorCard description="ID do evento não informado." />
      </div>
    )
  }

  if (eventQuery.isLoading) {
    return <PageLoading message="Carregando evento..." />
  }

  if (eventQuery.isError) {
    return (
      <div className={styles.page}>
        <ErrorCard description={getFriendlyErrorMessage(eventQuery.error)} onAction={eventQuery.refetch} />
      </div>
    )
  }

  const event = eventQuery.data

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
          <div className="flex flex-col gap-2 sm:flex-row">
            <ButtonLink size="sm" to={`/events/edit/${event.id}`} variant="primary">
              Editar evento
            </ButtonLink>
            <Button
              type="button"
              onClick={() => {
                const confirmed = globalThis.confirm("Deseja realmente arquivar este evento?")
                if (confirmed) {
                  deleteEventMutation.mutate()
                }
              }}
              disabled={deleteEventMutation.isPending}
              variant="error"
              size="sm"
            >
              Excluir Evento
            </Button>
          </div>
        }
        title="Resumo do evento"
        description="Dados completos do atendimento, participantes e valores."
      >
        {deleteEventMutation.isError ? (
          <div className="mb-4 alert alert-error text-sm">
            {getFriendlyErrorMessage(deleteEventMutation.error)}
          </div>
        ) : null}
        <div className={styles.summaryGrid}>
          {summaryItems.map((item) => (
            <SummaryItem key={item.label} label={item.label} value={item.value} />
          ))}
        </div>
      </SectionCard>
    </div>
  )
}
