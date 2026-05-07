import { PageLayout } from "@/components/layout/PageLayout";
import { Banknote, Receipt, CheckCircle2 } from "lucide-react";
import { ButtonLink } from "@/components/ui/button";
import { FinancialSummaryCards } from "../components/FinancialSummaryCards";
import { SectionCard } from "@/components/ui/section-card";

export function FinanceDashboardPage() {
  return (
    <PageLayout
      title="Painel Financeiro"
      description="Visão geral de entradas, saídas e saldos."
      Icon={Banknote}
      backLink="/"
    >
      <div className="flex flex-col gap-6">
        <FinancialSummaryCards />

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <SectionCard title="Gestão de Despesas" description="Registre e gerencie despesas administrativas.">
            <p className="text-sm text-base-content/70 mb-4">
              Registre e gerencie as despesas administrativas, como aluguel, luz, materiais e serviços.
            </p>
            <ButtonLink to="/finance/expenses" variant="primary">
              <Receipt className="h-4 w-4 mr-2" />
              Ver Despesas Gerais
            </ButtonLink>
          </SectionCard>

          <SectionCard title="Baixa de Atendimentos" description="Realize baixas financeiras de aulas.">
            <p className="text-sm text-base-content/70 mb-4">
              Realize a baixa financeira dos atendimentos concluídos para alunos e colaboradores.
            </p>
            <ButtonLink to="/finance/settlement" variant="success">
              <CheckCircle2 className="h-4 w-4 mr-2" />
              Dar Baixa em Aulas
            </ButtonLink>
          </SectionCard>
        </div>
      </div>
    </PageLayout>
  );
}
