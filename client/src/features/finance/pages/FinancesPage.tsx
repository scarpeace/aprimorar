import { PageLayout } from "@/components/layout/PageLayout";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { useGetFinanceSummary } from "@/kubb";
import { Landmark } from "lucide-react";
import { FinanceSummarySection } from "../components/FinanceSummarySection";

export function FinancesPage() {
  const summaryQuery = useGetFinanceSummary();

  const totalStudentCharged = summaryQuery.data?.totalStudentCharged ?? 0;
  const totalStudentPending = summaryQuery.data?.totalStudentPending ?? 0;
  const totalEmployeePaid = summaryQuery.data?.totalEmployeePaid ?? 0;
  const totalEmployeePending = summaryQuery.data?.totalEmployeePending ?? 0;
  const currentBalance = summaryQuery.data?.balance ?? 0;

  const headerProps = {
    description:
      "Acompanhe o panorama financeiro da instituicao com foco em fluxo, pendencias e movimentacoes.",
    title: "Financeiro",
    Icon: Landmark,
    backLink: "/",
  };

  if (summaryQuery.isPending) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando panorama financeiro" />
      </PageLayout>
    );
  }

  if (summaryQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Nao foi possivel carregar o financeiro"
          error={summaryQuery.error}
        />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <div className="flex w-full flex-col gap-4">
        <FinanceSummarySection
          currentBalance={currentBalance}
          totalStudentCharged={totalStudentCharged}
          totalStudentPending={totalStudentPending}
          totalEmployeePaid={totalEmployeePaid}
          totalEmployeePending={totalEmployeePending}
          isError={summaryQuery.isError}
          error={summaryQuery.error}
        />
      </div>
    </PageLayout>
  );
}
