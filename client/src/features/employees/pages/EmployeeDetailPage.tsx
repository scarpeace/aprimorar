import { Button } from "@/components/ui/button";
import { PageLayout } from "@/components/layout/PageLayout";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { KpiCard } from "@/components/ui/kpi-card";
import { EventsTable } from "@/features/events/components/EventsTable";
import { useGetEmployeeById, useGetEmployeeMonthlySummary, useGetEventsByEmployeeId } from "@/kubb";
import { brl, formatCpf, formatDateShortYear, formatPhone } from "@/lib/utils/formatter";
import { Edit, FileUser } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { ArchiveEmployeeButton } from "../components/ArchiveEmployeeButton";
import { DeleteEmployeeButton } from "../components/DeleteEmployeeButton";
import { EmployeeForm } from "../components/EmployeeForm";
import { dutyLabels } from "../utils/dutyLabels";
import { Badge } from "@/components/ui/badge";
import { ListSearchInput } from "@/components/ui/list-search-input";
import { useDebounce } from "@/lib/shared/use-debounce";

export function EmployeeDetailPage() {
  const { id } = useParams<{ id: string }>();
  const employeeId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);
  const [searchTerm, setSearchTerm] = useState("");
  const [isFormOpen, setIsFormOpen] = useState(false);

  const debouncedSearchTerm = useDebounce(searchTerm, 500);

  const employeeQuery = useGetEmployeeById(employeeId);
  const employeeEventsQuery = useGetEventsByEmployeeId(employeeId, {
    page: currentPage,
    studentName: debouncedSearchTerm,
    sort: ["startDate,desc"],
  });
  const employeeMonthlySummaryQuery = useGetEmployeeMonthlySummary(employeeId);

  const headerProps = {
    description: "Veja e gerencie as informações do colaborador",
    title: "Detalhes do colaborador",
    Icon: FileUser,
    backLink: "/employees",
  };

  if (employeeQuery.error) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do colaborador"
          error={employeeQuery.error}
        />
      </PageLayout>
    );
  }

  if (employeeQuery.isPending || !employeeQuery.data) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando detalhes do colaborador" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-3">
          <div className="lg:col-span-2">
            <SectionCard
              title="Colaborador"
              description="Dados do Colaborador"
              headerActions={
                <div className="flex gap-2 items-center flex-wrap justify-end">
                  <Button onClick={() => setIsFormOpen(true)} variant="primary">
                    <Edit className="h-4 w-4 mr-2" />Editar
                  </Button>

                  <ArchiveEmployeeButton
                    employeeId={employeeQuery.data.id}
                    isArchived={!!employeeQuery.data.archivedAt}
                  />
                  <DeleteEmployeeButton employeeId={employeeQuery.data.id} />
                </div>
              }
            >
             <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-3">
              <SummaryItem label="Nome completo" value={employeeQuery.data.name} />
              <SummaryItem label="E-mail" value={employeeQuery.data.email} />
              <SummaryItem label="Cargo" value={dutyLabels[employeeQuery.data.duty] ?? "Desconhecido"} />
              <SummaryItem label="Contato" value={formatPhone(employeeQuery.data.contact)} />
              <SummaryItem label="CPF" value={formatCpf(employeeQuery.data.cpf)} />
              <SummaryItem label="Data de nascimento" value={formatDateShortYear(employeeQuery.data.birthdate ?? "")} />
              <SummaryItem label="Chave PIX" value={employeeQuery.data.pix} />
              <SummaryItem label="Status" value={employeeQuery.data.archivedAt ? "Arquivado" : "Ativo"} />
              <SummaryItem className="grow" label="Criado em" value={formatDateShortYear(employeeQuery.data.createdAt ?? "")} />
             </div>
            </SectionCard>
          </div>

          <div className="lg:col-span-1">
            <SectionCard
              title={"Resumo mensal"}
              description={"Resumo dos atendimentos do colaborador no mês"}
            >
              {employeeMonthlySummaryQuery.isPending ? (
                <div className="flex flex-col gap-3">
                  <div className="h-24 w-full animate-pulse rounded-lg bg-base-200" />
                  <div className="h-24 w-full animate-pulse rounded-lg bg-base-200" />
                </div>
              ) : employeeMonthlySummaryQuery.isError ? (
                <div className="alert alert-error">
                  <span className="text-sm">Erro ao carregar o resumo mensal.</span>
                </div>
              ) : (
                <div className="grid gap-3">
                  <KpiCard
                    label="Total de atendimentos"
                    value={employeeMonthlySummaryQuery.data?.totalEvents ?? 0}
                  />
                  <KpiCard
                    label="Valor a receber"
                    value={brl.format(employeeMonthlySummaryQuery.data?.totalPayment ?? 0)}
                  />
                </div>
              )}
            </SectionCard>
          </div>
        </div>

        <SectionCard
          title={"Atendimentos"}
          description={"Atendimentos vinculados ao colaborador"}
          headerActions={
            <ListSearchInput
              placeholder="Buscar aluno"
              value={searchTerm}
              onChange={(val) => {
                setSearchTerm(val);
                setCurrentPage(0);
              }}
            />
          }
        >
          <EventsTable
            eventsPage={employeeEventsQuery.data}
            isPending={employeeEventsQuery.isPending}
            error={employeeEventsQuery.error}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
        </SectionCard>

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-2xl">
              <h3 className="font-bold text-lg mb-4">Editar Colaborador</h3>
              <EmployeeForm
                initialData={employeeQuery.data}
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
