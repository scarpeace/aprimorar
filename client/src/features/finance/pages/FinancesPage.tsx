import { DateRangeInput } from "@/components/ui/date-range-input";
import { PageLayout } from "@/components/layout/PageLayout";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import {
  useGetAppointmentFinanceReport,
  useGetEmployeesAppointmentsFinanceReport,
  useGetStudentsAppointmentsFinanceReport,
} from "@/kubb";
import { useDateRangeFilters } from "@/hooks/use-date-range-filters";
import { Landmark } from "lucide-react";
import { EmployeesFinanceTable } from "../components/EmployeesFinanceTable";
import { FinanceSummarySection } from "../components/FinanceSummarySection";
import { StudentsFinanceTable } from "../components/StudentsFinanceTable";

export function FinancesPage() {
  const {
    startDate,
    endDate,
    handleStartDateChange,
    handleEndDateChange,
  } = useDateRangeFilters();

  const summaryQuery = useGetAppointmentFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });
  const studentsFinanceQuery = useGetStudentsAppointmentsFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });
  const employeesFinanceQuery = useGetEmployeesAppointmentsFinanceReport({
    startDate: startDate?.toISOString(),
    endDate: endDate?.toISOString(),
  });

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
        <div className="relative z-10 flex flex-col gap-3 rounded-2xl border border-base-300 bg-base-100 p-4 shadow-sm animate-[fade-up_200ms_ease-out_both] sm:flex-row sm:items-center sm:justify-between">
          <div>
            <h2 className="text-xl font-bold text-base-content">
              Resumo Institucional
            </h2>
            <p className="mt-1 text-sm text-base-content/60">
              Visualize o consolidado financeiro filtrado por periodo.
            </p>
          </div>

          <div className="w-full sm:w-auto">
            <DateRangeInput
              startDate={startDate}
              endDate={endDate}
              onStartDateChange={handleStartDateChange}
              onEndDateChange={handleEndDateChange}
            />
          </div>
        </div>

        <FinanceSummarySection
          currentBalance={currentBalance}
          totalStudentCharged={totalStudentCharged}
          totalStudentPending={totalStudentPending}
          totalEmployeePaid={totalEmployeePaid}
          totalEmployeePending={totalEmployeePending}
          isError={summaryQuery.isError}
          error={summaryQuery.error}
        />

        <div className="grid gap-4 xl:grid-cols-2">
          <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
            <div className="mb-4">
              <div className="flex flex-wrap items-center gap-2">
                <span className="badge badge-primary badge-outline px-3 py-3">
                  Alunos
                </span>
              </div>
              <h2 className="mt-3 text-2xl font-bold text-base-content">
                Financeiro por aluno
              </h2>
              <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
                Valores recebidos e pendentes por aluno no periodo selecionado.
              </p>
            </div>

            <StudentsFinanceTable
              students={studentsFinanceQuery.data?.students}
              isPending={studentsFinanceQuery.isPending}
              error={studentsFinanceQuery.error}
            />
          </section>

          <section className="rounded-2xl border border-base-300 bg-base-100 p-5 shadow-sm animate-[fade-up_200ms_ease-out_both]">
            <div className="mb-4">
              <div className="flex flex-wrap items-center gap-2">
                <span className="badge badge-primary badge-outline px-3 py-3">
                  Colaboradores
                </span>
              </div>
              <h2 className="mt-3 text-2xl font-bold text-base-content">
                Financeiro por colaborador
              </h2>
              <p className="mt-1 max-w-3xl text-sm leading-6 text-base-content/60">
                Valores pagos e pendentes por colaborador no periodo selecionado.
              </p>
            </div>

            <EmployeesFinanceTable
              employees={employeesFinanceQuery.data?.employees}
              isPending={employeesFinanceQuery.isPending}
              error={employeesFinanceQuery.error}
            />
          </section>
        </div>
      </div>
    </PageLayout>
  );
}
