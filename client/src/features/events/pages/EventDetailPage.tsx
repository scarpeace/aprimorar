import { Alert } from "@/components/ui/alert";
import { Badge } from "@/components/ui/badge";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetEventById } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, Edit, CheckCircle2, XCircle } from "lucide-react";
import { useParams } from "react-router-dom";
import { useEventMutations } from "../hooks/use-event-mutations";
import { EventStatusLabels } from "@/lib/shared/eventStatusLabels";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { useState } from "react";
import { EventForm } from "../components/EventForm";

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const eventQuery = useGetEventById(eventId);
  const { updateEvent } = useEventMutations();

  const handleStatusChange = (newStatus: "SCHEDULED" | "COMPLETED" | "CANCELED") => {
    if (!eventQuery.data) return;

    updateEvent.mutate({
      eventId,
      data: {
        studentId: eventQuery.data.studentId,
        employeeId: eventQuery.data.employeeId,
        startDate: eventQuery.data.startDate,
        endDate: eventQuery.data.endDate,
        payment: eventQuery.data.payment,
        price: eventQuery.data.price,
        content: eventQuery.data.content,
        description: eventQuery.data.description,
        status: newStatus,
      },
    });
  };

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
  const isCanceled = eventQuery.data.status === "CANCELED";
  const isCompleted = eventQuery.data.status === "COMPLETED";

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-4 animate-[fade-up_300ms_ease-out_both]">
        {isCanceled && (
          <Alert variant="error" title="Atendimento Cancelado">
            Este atendimento foi marcado como cancelado e não será contabilizado em certas métricas ou conflitos de horário.
          </Alert>
        )}

        <SectionCard
          title="Resumo do evento"
          description="Dados completos do atendimento, participantes e valores."
          headerActions={
            <div className="flex flex-wrap gap-2 items-center justify-end">
              {!isCompleted && !isCanceled && (
                <Button
                  variant="success"
                  size="sm"
                  onClick={() => handleStatusChange("COMPLETED")}
                  disabled={updateEvent.isPending}
                >
                  <CheckCircle2 className="h-4 w-4 mr-1" /> Concluir
                </Button>
              )}
              {!isCanceled && (
                <Button
                  variant="error"
                  size="sm"
                  onClick={() => handleStatusChange("CANCELED")}
                  disabled={updateEvent.isPending}
                >
                  <XCircle className="h-4 w-4 mr-1" /> Cancelar
                </Button>
              )}
              {isCanceled && (
                <Button
                  variant="outline"
                  size="sm"
                  onClick={() => handleStatusChange("SCHEDULED")}
                  disabled={updateEvent.isPending}
                >
                  <Calendar className="h-4 w-4 mr-1" /> Re-agendar
                </Button>
              )}
              <Button onClick={() => setIsFormOpen(true)} variant="primary" size="sm">
                <Edit className="h-4 w-4 mr-1" /> Editar
              </Button>
            </div>
          }
        >
          <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
            <SummaryItem
              label="Status"
              value={
                <Badge
                  variant={
                    eventQuery.data.status === "COMPLETED"
                      ? "success"
                      : eventQuery.data.status === "CANCELED"
                      ? "error"
                      : "primary"
                  }
                >
                  {EventStatusLabels[eventQuery.data.status] || eventQuery.data.status}
                </Badge>
              }
            />
            <SummaryItem label="Data" value={formatDateShortYear(eventQuery.data.startDate)}/>
            <div className="flex gap-3">
              <SummaryItem className="w-full" label="Horário início" value={formatTime(eventQuery.data.startDate)} />
              <SummaryItem className="w-full" label="Horário fim" value={formatTime(eventQuery.data.endDate)}/>
            </div>
            <SummaryItem label="Nome do aluno" value={eventQuery.data.studentName} />
            <SummaryItem label="Nome do colaborador" value={eventQuery.data.employeeName}/>
            <SummaryItem label="Conteúdo" value={EventContentLabels[eventQuery.data.content] || eventQuery.data.content} />
            <div className="flex gap-3">
              <SummaryItem className="w-full" label="Valor" value={brl.format(eventQuery.data.price)} />
              <SummaryItem className="w-full" label="Pagamento" value={brl.format(eventQuery.data.payment)}/>
              <SummaryItem className="w-full" label="Lucro" value={brl.format(profit)} />
            </div>
            <SummaryItem className="md:col-span-3 h-30" label="Observações" value={eventQuery.data.description}/>
        </div>
        </SectionCard>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-4xl">
              <h3 className="font-bold text-lg mb-4">Editar Atendimento</h3>
              <EventForm
                initialData={eventQuery.data}
                onSuccess={() => setIsFormOpen(false)}
                onCancel={() => setIsFormOpen(false)}
              />
            </div>
          </div>
        )}
      </div>
    </PageLayout>
  );
}
