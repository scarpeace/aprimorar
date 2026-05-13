import { Badge } from "@/components/ui/badge";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { useGetAppointmentById } from "@/kubb";
import { brl, formatDateShortYear, formatTime } from "@/lib/utils/formatter";
import { Calendar, Check, CircleDollarSign, Clock3, Edit, GraduationCap, UserRoundCog } from "lucide-react";
import { useParams } from "react-router-dom";
import { useAppointmentMutations } from "../hooks/use-appointment-mutations";
import { EventContentLabels } from "@/lib/shared/eventContentLables";
import { useState } from "react";
import { AppointmentForm } from "../components/AppointmentForm";

export function AppointmentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const appointmentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const eventQuery = useGetAppointmentById(appointmentId);
  const { toggleStudentCharge, toggleEmployeePayment } = useAppointmentMutations();

  const headerProps = {
    description: "Veja e gerencie as informações do evento",
    Icon: Calendar,
    title: "Detalhes do evento",
    backLink: "/appointments",
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
      id: eventQuery.data.id,
    });
  };

  const handleToggleExpenseStatus = () => {
    toggleEmployeePayment.mutate({
      id: eventQuery.data.id,
    });
  };

  return (
    <PageLayout {...headerProps}>
      <div className="flex flex-col gap-4 animate-[fade-up_300ms_ease-out_both]">
        <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm">
          <div className="flex flex-col gap-5 lg:flex-row lg:items-start lg:justify-between">
            <div className="space-y-4">
              <div className="flex flex-wrap items-center gap-2">
                <Badge variant="primary" className="px-3 py-1 font-semibold">
                  {EventContentLabels[eventQuery.data.content] || eventQuery.data.content}
                </Badge>
                <Badge variant={eventQuery.data.studentChargeDate != null ? "success" : "warning"} className="px-3 py-1 font-semibold">
                  {eventQuery.data.studentChargeDate != null ? "Aluno cobrado" : "Cobranca pendente"}
                </Badge>
                <Badge variant={eventQuery.data.employeePaymentDate != null ? "success" : "warning"} className="px-3 py-1 font-semibold">
                  {eventQuery.data.employeePaymentDate != null ? "Colaborador pago" : "Repasse pendente"}
                </Badge>
              </div>

              <div>
                <h2 className="text-2xl font-bold text-base-content">{eventQuery.data.studentName}</h2>
                <p className="mt-1 text-sm text-base-content/60">
                  Atendimento conduzido por {eventQuery.data.employeeName} em {formatDateShortYear(eventQuery.data.startDate)}.
                </p>
              </div>

              <div className="grid gap-3 sm:grid-cols-3">
                <div className="rounded-2xl border border-base-300 bg-base-200/40 p-3 shadow-sm">
                  <div className="mb-2 flex items-center gap-2 text-sm font-semibold text-base-content/70">
                    <Calendar className="h-4 w-4 text-primary" />
                    Data
                  </div>
                  <div className="text-base font-semibold text-base-content">
                    {formatDateShortYear(eventQuery.data.startDate)}
                  </div>
                </div>
                <div className="rounded-2xl border border-base-300 bg-base-200/40 p-3 shadow-sm">
                  <div className="mb-2 flex items-center gap-2 text-sm font-semibold text-base-content/70">
                    <Clock3 className="h-4 w-4 text-primary" />
                    Horario
                  </div>
                  <div className="text-base font-semibold text-base-content">
                    {formatTime(eventQuery.data.startDate)} - {formatTime(eventQuery.data.endDate)}
                  </div>
                </div>
                <div className="rounded-2xl border border-base-300 bg-base-200/40 p-3 shadow-sm">
                  <div className="mb-2 flex items-center gap-2 text-sm font-semibold text-base-content/70">
                    <CircleDollarSign className="h-4 w-4 text-primary" />
                    Lucro
                  </div>
                  <div className="text-base font-semibold text-success">
                    {brl.format(eventQuery.data.profit)}
                  </div>
                </div>
              </div>
            </div>

            <div className="rounded-2xl border border-base-300 bg-linear-to-br from-primary/6 to-base-100 p-3 shadow-sm">
              <Button
                onClick={() => setIsFormOpen(true)}
                variant="primary"
                size="sm"
                className="w-full lg:w-auto"
              >
                <Edit className="mr-1 h-4 w-4" /> Editar atendimento
              </Button>
            </div>
          </div>
        </section>

        <SectionCard
          title="Resumo do evento"
          description="Dados completos do atendimento, participantes e valores financeiros." 
        >
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <SummaryItem
              label="Aluno"
              value={
                <div className="flex items-center gap-2 font-medium">
                  <GraduationCap className="h-4 w-4 text-primary" />
                  <span>{eventQuery.data.studentName}</span>
                </div>
              }
            />
            <SummaryItem
              label="Colaborador"
              value={
                <div className="flex items-center gap-2 font-medium">
                  <UserRoundCog className="h-4 w-4 text-secondary" />
                  <span>{eventQuery.data.employeeName}</span>
                </div>
              }
            />
            <SummaryItem
              label="Conteúdo"
              value={
                EventContentLabels[eventQuery.data.content] ||
                eventQuery.data.content
              }
            />

            <SummaryItem
              label="Receita do aluno"
              value={
                <div className="flex items-center justify-between gap-3">
                  <div>
                    <div className="font-mono text-xl font-bold text-base-content">
                      {brl.format(eventQuery.data.price)}
                    </div>
                    <div className="text-[10px] font-bold uppercase opacity-50">
                      Valor cobrado do aluno
                    </div>
                  </div>
                  <Badge variant={eventQuery.data.studentChargeDate != null ? "success" : "warning"}>
                    {eventQuery.data.studentChargeDate != null ? "Pago" : "Pendente"}
                  </Badge>
                </div>
              }
            />

            <SummaryItem
              label="Repasse do colaborador"
              value={
                <div className="flex items-center justify-between gap-3">
                  <div>
                    <div className="font-mono text-xl font-bold text-base-content">
                      {brl.format(eventQuery.data.payment)}
                    </div>
                    <div className="text-[10px] font-bold uppercase opacity-50">
                      Valor do repasse
                    </div>
                  </div>
                  <Badge variant={eventQuery.data.employeePaymentDate != null ? "success" : "warning"}>
                    {eventQuery.data.employeePaymentDate != null ? "Pago" : "Pendente"}
                  </Badge>
                </div>
              }
            />

            <SummaryItem
              label="Lucro líquido"
              value={
                <div className="flex flex-col gap-1">
                  <span className="text-2xl font-bold text-success">
                    {brl.format(eventQuery.data.profit)}
                  </span>
                  <span className="text-[10px] uppercase opacity-50 font-bold">
                    Resultado líquido do atendimento
                  </span>
                </div>
              }
            />

            <SummaryItem
              label="Cobrança do aluno"
              value={
                <div className="flex flex-col gap-3">
                  <div className="flex items-center justify-between gap-2">
                    <Badge variant={eventQuery.data.studentChargeDate != null ? "success" : "warning"}>
                      {eventQuery.data.studentChargeDate != null ? "Pago" : "Pendente"}
                    </Badge>
                    <Button
                      size="sm"
                      variant={eventQuery.data.studentChargeDate != null ? "outline" : "success"}
                      onClick={handleToggleIncomeStatus}
                      disabled={toggleStudentCharge.isPending}
                      className="h-8 px-3 text-xs"
                    >
                      {eventQuery.data.studentChargeDate != null ? (
                        <Clock3 className="mr-1 h-3 w-3" />
                      ) : (
                        <Check className="mr-1 h-3 w-3" />
                      )}
                      {eventQuery.data.studentChargeDate != null ? "Marcar pendente" : "Marcar pago"}
                    </Button>
                  </div>
                  <div className="rounded-xl border border-base-300/60 bg-base-200/30 p-3 text-xs text-base-content/65">
                    Alterne o status assim que a cobrança do aluno for confirmada.
                  </div>
                </div>
              }
            />

            <SummaryItem
              label="Repasse do colaborador"
              value={
                <div className="flex flex-col gap-3">
                  <div className="flex items-center justify-between gap-2">
                    <Badge variant={eventQuery.data.employeePaymentDate != null ? "success" : "warning"}>
                      {eventQuery.data.employeePaymentDate != null ? "Pago" : "Pendente"}
                    </Badge>
                    <Button
                      size="sm"
                      variant={eventQuery.data.employeePaymentDate != null ? "outline" : "success"}
                      onClick={handleToggleExpenseStatus}
                      disabled={toggleEmployeePayment.isPending}
                      className="h-8 px-3 text-xs"
                    >
                      {eventQuery.data.employeePaymentDate != null ? (
                        <Clock3 className="mr-1 h-3 w-3" />
                      ) : (
                        <Check className="mr-1 h-3 w-3" />
                      )}
                      {eventQuery.data.employeePaymentDate != null ? "Marcar pendente" : "Marcar pago"}
                    </Button>
                  </div>
                  <div className="rounded-xl border border-base-300/60 bg-base-200/30 p-3 text-xs text-base-content/65">
                    Atualize o repasse quando o pagamento do colaborador for realizado.
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
          <div className="modal-box max-w-4xl border border-base-300 bg-base-100 shadow-2xl">
            <h3 className="mb-1 text-lg font-bold">Editar Atendimento</h3>
            <p className="mb-4 text-sm text-base-content/60">
              Ajuste participantes, horario e valores para manter agenda e financeiro sincronizados.
            </p>
            <AppointmentForm
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
