import { PageLayout } from "@/components/layout/PageLayout";
import { Modal } from "@/components/ui/modal";
import { AtendimentosCalendar } from "../components/AtendimentosCalendar";
import { lazy, Suspense, useState } from "react";
import { CalendarDays, Plus } from "lucide-react";
import { AtendimentoContentLegend } from "../components/AtendimentoContentLegend";
import { Button } from "@/components/ui/button";

const AtendimentoForm = lazy(() => import("@/features/atendimentos/components/AtendimentoForm").then((module) => ({ default: module.AtendimentoForm })));

export function DashboardPage() {
  const [isFormOpen, setIsFormOpen] = useState(false);

  const headerProps = {
    description: "Visão geral da operação do Aprimorar",
    title: "Dashboard",
    Icon: CalendarDays,
    backLink: "/",
    iconBg: "primary",
  } as const;

  const handleCloseForm = () => {
    setIsFormOpen(false);
  };

  return (
    <>
      <PageLayout {...headerProps}>
        <Button onClick={() => setIsFormOpen(true)} variant="success"><Plus className="mr-2 h-4 w-4" />Novo atendimento</Button>
        <AtendimentoContentLegend/>
        <AtendimentosCalendar/>

      <Modal
        isOpen={isFormOpen}
        onClose={handleCloseForm}
        title="Cadastrar Novo Atendimento"
        description="Defina aluno, colaborador, horario e valores do atendimento para manter agenda e financeiro sincronizados."
        size="lg"
      >
        <Suspense fallback={<p className="text-sm text-base-content/60">Carregando formulário...</p>}>
          <AtendimentoForm
            initialData={null}
            onSuccess={handleCloseForm}
            onCancel={handleCloseForm}
          />
        </Suspense>
      </Modal>

      </PageLayout>
      </>
  );
}
