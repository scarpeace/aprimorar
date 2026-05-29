import { Badge } from "@/components/ui/badge";
import { PageLayout } from "@/components/layout/PageLayout";
import { Button } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { useGetAtendimentoById } from "@/kubb";
import { Calendar, Edit } from "lucide-react";
import { useParams } from "react-router-dom";
import { useAtendimentoMutations } from "../hooks/use-atendimento-mutations";
import { EventContentLabels } from "@/features/atendimentos/lib/eventContentLabels";
import { useState } from "react";
import { AtendimentoForm } from "../components/AtendimentoForm";
import { AtendimentoInfoSection } from "../components/AtendimentoInfoSection";
import { ToggleAlunoChargeButton } from "../components/ToggleAlunoChargeButton";
import { ToggleColaboradorPaymentButton } from "../components/ToggleColaboradorPaymentButton";

export function AtendimentoDetailPage() {
  const { id } = useParams<{ id: string }>();
  const atendimentoId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);

  const atendimentoQuery = useGetAtendimentoById(atendimentoId);
  const { toggleStudentCharge, toggleEmployeePayment } =
    useAtendimentoMutations();

  const headerProps = {
    description: "Veja e gerencie as informações do atendimentoo",
    Icon: Calendar,
    title: "Detalhes do atendimentoo",
    backLink: "/atendimentos",
    iconBg: "secondary",
  } as const;

  if (atendimentoQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar atendimentoo"
          error={atendimentoQuery.error}
        />
      </PageLayout>
    );
  }

  if (atendimentoQuery.isPending || !atendimentoQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando dados do atendimentoo" />
      </PageLayout>
    );
  }

  const handleToggleIncomeStatus = () => {
    toggleStudentCharge.mutate({
      id: atendimentoQuery.data.id,
    });
  };

  const handleToggleExpenseStatus = () => {
    toggleEmployeePayment.mutate({
      id: atendimentoQuery.data.id,
    });
  };

  const contentLabel =
    EventContentLabels[atendimentoQuery.data.content] ||
    atendimentoQuery.data.content;

  const alunoChargePaid = atendimentoQuery.data.studentChargeDate != null;
  const colaboradorPaymentPaid = atendimentoQuery.data.employeePaymentDate != null;

  return (
    <PageLayout {...headerProps}>
      <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_300ms_ease-out_both] lg:justify-between">

        <div className="flex justify-between items-center mb-3">
          <div className="flex flex-row gap-3">
              <Badge variant="primary" className="px-3 py-1 font-semibold">
                {contentLabel}
              </Badge>
              <Badge
                variant={alunoChargePaid ? "success" : "warning"}
                className="px-3 py-1 font-semibold"
              >
                {alunoChargePaid ? "Aluno cobrado" : "Cobranca pendente"}
              </Badge>
              <Badge
                variant={colaboradorPaymentPaid ? "success" : "warning"}
                className="px-3 py-1 font-semibold"
              >
                {colaboradorPaymentPaid ? "Colaborador pago" : "Repasse pendente"}
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
                <ToggleAlunoChargeButton
                  alunoChargePaid={alunoChargePaid}
                  toggleStudentCharge={toggleStudentCharge}
                  handleToggleIncomeStatus={handleToggleIncomeStatus}
                />
                <ToggleColaboradorPaymentButton
                  colaboradorPaymentPaid={colaboradorPaymentPaid}
                  toggleEmployeePayment={toggleEmployeePayment}
                  handleToggleEmployeePayment={handleToggleExpenseStatus}
                />
              </div>
        </div>

          <AtendimentoInfoSection atendimento={atendimentoQuery.data} />
        </section>

      {isFormOpen && (
        <div className="modal modal-open">
          <div className="modal-box max-w-4xl border border-base-300 bg-base-100 shadow-2xl">
            <h3 className="mb-1 text-lg font-bold">Editar Atendimento</h3>
            <p className="mb-4 text-sm text-base-content/60">
              Ajuste participantes, horario e valores para manter agenda e
              financeiro sincronizados.
            </p>
            <AtendimentoForm
              initialData={atendimentoQuery.data}
              onSuccess={() => setIsFormOpen(false)}
              onCancel={() => setIsFormOpen(false)}
            />
          </div>
        </div>
      )}
    </PageLayout>
  );
}
