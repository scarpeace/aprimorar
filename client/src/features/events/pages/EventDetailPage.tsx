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
  const { toggleStudentCharge, toggleEmployeePayment } = useEventMutations();

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

  const handleToggleIncomeStatus = () => {
    toggleStudentCharge.mutate({
      id: eventQuery.data.eventId,
    });
  };

  const handleToggleExpenseStatus = () => {
    toggleEmployeePayment.mutate({
      id: eventQuery.data.eventId,
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
              <Button
                onClick={() => setIsFormOpen(true)}
                variant="primary"
                size="sm"
              >
                <Edit className="h-4 w-4 mr-1" /> Editar
              </Button>
            </div>
          }
        >
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            {/* Informações Básicas */}
            <SummaryItem
              label="Data"
              value={formatDateShortYear(eventQuery.data.startDate)}
            />
            <SummaryItem
              label="Início"
              value={formatTime(eventQuery.data.startDate)}
            />
            <SummaryItem
              label="Fim"
              value={formatTime(eventQuery.data.endDate)}
            />

            <SummaryItem
              label="Nome do aluno"
              value={eventQuery.data.studentName}
            />
            <SummaryItem
              label="Nome do colaborador"
              value={eventQuery.data.employeeName}
            />
            <SummaryItem
              label="Conteúdo"
              value={
                EventContentLabels[eventQuery.data.content] ||
                eventQuery.data.content
              }
            />

            {/* Linha Financeira */}
            <SummaryItem
              label="Lucro"
              value={
                <div className="flex flex-col gap-1">
                  <span className="text-2xl font-bold text-success">
                    {brl.format(eventQuery.data.profit)}
                  </span>
                  <span className="text-[10px] uppercase opacity-50 font-bold">
                    Resultado líquido
                  </span>
                </div>
              }
            />

            <SummaryItem
              label="Recebimento do aluno"
              value={
                <div className="flex flex-col gap-3">
                  <div className="flex items-center justify-between gap-2">
                    <Badge variant={eventQuery.data.studentCharged ? "success" : "warning"}>
                      {eventQuery.data.studentCharged ? "Pago" : "Pendente"}
                    </Badge>
                    <Button
                      size="sm"
                      variant={eventQuery.data.studentCharged ? "outline" : "success"}
                      onClick={handleToggleIncomeStatus}
                      disabled={toggleStudentCharge.isPending}
                      className="h-7 px-2 text-xs"
                    >
                      {eventQuery.data.studentCharged ? (
                        <Clock3 className="h-3 w-3 mr-1" />
                      ) : (
                        <Check className="h-3 w-3 mr-1" />
                      )}
                      {eventQuery.data.studentCharged ? "Marcar Pendente" : "Marcar Pago"}
                    </Button>
                  </div>
                  <div className="p-2 bg-base-300/30 rounded border border-base-300/50">
                    <p className="text-[10px] uppercase opacity-60 font-bold">
                      Valor
                    </p>
                    <p className="text-lg font-bold">
                      {brl.format(eventQuery.data.price)}
                    </p>
                  </div>
                </div>
              }
            />

            <SummaryItem
              label="Pagamento do colaborador"
              value={
                <div className="flex flex-col gap-3">
                  <div className="flex items-center justify-between gap-2">
                    <Badge variant={eventQuery.data.employeePaid ? "success" : "warning"}>
                      {eventQuery.data.employeePaid ? "Pago" : "Pendente"}
                    </Badge>
                    <Button
                      size="sm"
                      variant={eventQuery.data.employeePaid ? "outline" : "success"}
                      onClick={handleToggleExpenseStatus}
                      disabled={toggleEmployeePayment.isPending}
                      className="h-7 px-2 text-xs"
                    >
                      {eventQuery.data.employeePaid ? (
                        <Clock3 className="h-3 w-3 mr-1" />
                      ) : (
                        <Check className="h-3 w-3 mr-1" />
                      )}
                      {eventQuery.data.employeePaid ? "Marcar Pendente" : "Marcar Pago"}
                    </Button>
                  </div>
                  <div className="p-2 bg-base-300/30 rounded border border-base-300/50">
                    <p className="text-[10px] uppercase opacity-60 font-bold">
                      Pagamento
                    </p>
                    <p className="text-lg font-bold">
                      {brl.format(eventQuery.data.payment)}
                    </p>
                  </div>
                </div>
              }
            />

            <SummaryItem
              className="md:col-span-3 min-h-25"
              label="Observações"
              value={eventQuery.data.description || "Nenhuma observação."}
            />
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
