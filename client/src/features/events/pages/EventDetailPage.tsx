import { Badge } from "@/components/ui/badge";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetEventById } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, Check, Edit, Clock3 } from "lucide-react";
import { useParams } from "react-router-dom";
import { useEventMutations } from "../hooks/use-event-mutations";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { useState } from "react";
import { EventForm } from "../components/EventForm";

export function EventDetailPage() {
  const { id } = useParams<{ id: string }>();
  const eventId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const eventQuery = useGetEventById(eventId);
  const { settleStudentCharge, settleEmployeePayment } = useEventMutations({ onSuccessCallback: () => {} });

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
  const isIncomePaid = eventQuery.data.studentCharged;
  const isExpensePaid = eventQuery.data.employeePaid;
  const isFinancialPending = settleStudentCharge.isPending || settleEmployeePayment.isPending;

  const handleToggleIncomeStatus = () => {
    settleStudentCharge.mutate({
      id: eventQuery.data.eventId,
      params: { charged: !isIncomePaid },
    });
  };

  const handleToggleExpenseStatus = () => {
    settleEmployeePayment.mutate({
      id: eventQuery.data.eventId,
      params: { paid: !isExpensePaid },
    });
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-4 animate-[fade-up_300ms_ease-out_both]">
        <SectionCard
          title="Resumo do evento"
          description="Dados completos do atendimento, participantes e valores."
          headerActions={
            <div className="flex flex-wrap gap-2 items-center justify-end">
              <Button onClick={() => setIsFormOpen(true)} variant="primary" size="sm">
                <Edit className="h-4 w-4 mr-1" /> Editar
              </Button>
            </div>
          }
        >
          <div className="grid grid-cols-1 gap-4 md:grid-cols-3">
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
            <SummaryItem
              label="Recebimento do aluno"
              value={
                <div className="flex flex-wrap items-center gap-3">
                  <Badge variant={isIncomePaid ? "success" : "warning"}>
                    {isIncomePaid ? "Pago" : "Pendente"}
                  </Badge>
                  <Button
                    size="sm"
                    variant={isIncomePaid ? "outline" : "success"}
                    onClick={handleToggleIncomeStatus}
                    disabled={isFinancialPending}
                  >
                    {isIncomePaid ? <Clock3 className="h-4 w-4 mr-1" /> : <Check className="h-4 w-4 mr-1" />}
                    {isIncomePaid ? "Marcar como pendente" : "Marcar como pago"}
                  </Button>
                </div>
              }
            />
            <SummaryItem
              label="Pagamento do colaborador"
              value={
                <div className="flex flex-wrap items-center gap-3">
                  <Badge variant={isExpensePaid ? "success" : "warning"}>
                    {isExpensePaid ? "Pago" : "Pendente"}
                  </Badge>
                  <Button
                    size="sm"
                    variant={isExpensePaid ? "outline" : "success"}
                    onClick={handleToggleExpenseStatus}
                    disabled={isFinancialPending}
                  >
                    {isExpensePaid ? <Clock3 className="h-4 w-4 mr-1" /> : <Check className="h-4 w-4 mr-1" />}
                    {isExpensePaid ? "Marcar como pendente" : "Marcar como pago"}
                  </Button>
                </div>
              }
            />
            <SummaryItem className="md:col-span-3 h-30" label="Observações" value={eventQuery.data.description}/>
        </div>
        </SectionCard>
      </div>
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
    </PageLayout>
  );
}
