import { Badge } from "@/components/ui/badge";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { useGetAtendimentoById } from "@/kubb";
import { Calendar, Edit } from "lucide-react";
import { useParams } from "react-router-dom";
import { useAppointmentMutations } from "../hooks/use-appointment-mutations";
import { EventContentLabels } from "@/features/appointments/lib/eventContentLables.ts";
import { useState } from "react";
import { AppointmentForm } from "../components/AppointmentForm";
import { AppointmentInfoSection } from "../components/AppointmentInfoSection";
import { ToggleStudentChargeButton } from "../components/ToggleStudentChargeButton";
import { ToggleEmployeePaymentButton } from "../components/ToggleEmployeePaymentButton";

export function AppointmentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const appointmentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const appointmentQuery = useGetAtendimentoById(appointmentId);
  const { toggleStudentCharge, toggleEmployeePayment } =
    useAppointmentMutations();

  const headerProps = {
    description: "Veja e gerencie as informações do evento",
    Icon: Calendar,
    title: "Detalhes do evento",
    backLink: "/appointments",
  };

  if (appointmentQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar evento"
          error={appointmentQuery.error}
        />
      </PageLayout>
    );
  }

  if (appointmentQuery.isPending || !appointmentQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do evento" />
      </PageLayout>
    );
  }

  const handleToggleIncomeStatus = () => {
    toggleStudentCharge.mutate({
      id: appointmentQuery.data.id,
    });
  };

  const handleToggleExpenseStatus = () => {
    toggleEmployeePayment.mutate({
      id: appointmentQuery.data.id,
    });
  };

  const contentLabel =
    EventContentLabels[appointmentQuery.data.content] ||
    appointmentQuery.data.content;

  const studentChargePaid = appointmentQuery.data.studentChargeDate != null;
  const employeePaymentPaid = appointmentQuery.data.employeePaymentDate != null;

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_300ms_ease-out_both] lg:justify-between">

        <div className="flex justify-between items-center mb-3">
          <div className="flex flex-row gap-3">
              <Badge variant="primary" className="px-3 py-1 font-semibold">
                {contentLabel}
              </Badge>
              <Badge
                variant={studentChargePaid ? "success" : "warning"}
                className="px-3 py-1 font-semibold"
              >
                {studentChargePaid ? "Aluno cobrado" : "Cobranca pendente"}
              </Badge>
              <Badge
                variant={employeePaymentPaid ? "success" : "warning"}
                className="px-3 py-1 font-semibold"
              >
                {employeePaymentPaid ? "Colaborador pago" : "Repasse pendente"}
            </Badge>
            </div>


              <div className="flex flex-row gap-2 sm:flex-row sm:flex-wrap">
                <Button
                  onClick={() => setIsFormOpen(true)}
                  variant="primary"
                  size="sm"
                  className="w-full sm:w-auto"
                >
                  <Edit className="mr-1 h-4 w-4" /> Editar
                </Button>
                <ToggleStudentChargeButton
                  studentChargePaid={studentChargePaid}
                  toggleStudentCharge={toggleStudentCharge}
                  handleToggleIncomeStatus={handleToggleIncomeStatus}
                />
                <ToggleEmployeePaymentButton
                  employeePaymentPaid={employeePaymentPaid}
                  toggleEmployeePayment={toggleEmployeePayment}
                  handleToggleEmployeePayment={handleToggleExpenseStatus}
                />
              </div>
        </div>

          <AppointmentInfoSection appointment={appointmentQuery.data} />
        </section>

      {isFormOpen && (
        <div className="modal modal-open">
          <div className="modal-box max-w-4xl border border-base-300 bg-base-100 shadow-2xl">
            <h3 className="mb-1 text-lg font-bold">Editar Atendimento</h3>
            <p className="mb-4 text-sm text-base-content/60">
              Ajuste participantes, horario e valores para manter agenda e
              financeiro sincronizados.
            </p>
            <AppointmentForm
              initialData={appointmentQuery.data}
              onSuccess={() => setIsFormOpen(false)}
              onCancel={() => setIsFormOpen(false)}
            />
          </div>
        </div>
      )}
    </PageLayout>
  );
}
