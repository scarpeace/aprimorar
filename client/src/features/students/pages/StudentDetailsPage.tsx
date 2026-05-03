import { Button } from "@/components/ui/button";
import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { PageLayout } from "@/components/layout/PageLayout";
import { AddressDetails } from "@/features/address/components/AddressDetails";
import { useGetStudentById } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { BrushCleaning, Edit, GraduationCap } from "lucide-react";
import { useState } from "react";
import { useParams, useSearchParams } from "react-router-dom";
import { ArchiveStudentButton } from "../components/ArchiveStudentButton";
import { DeleteStudentButton } from "../components/DeleteStudentButton";
import { StudentForm } from "../components/StudentForm";
import { StudentKPIs } from "../components/StudentKPIs";
import { StudentEventsTable } from "../components/StudentEventsTable";
import { DateRangeInput } from "@/components/ui/date-range-input";

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();

  const startDateStr = searchParams.get("startDate");
  const endDateStr = searchParams.get("endDate");

  const startDate = startDateStr ? new Date(startDateStr) : undefined;
  const endDate = endDateStr ? new Date(endDateStr) : undefined;

  const handleStartDateChange = (date: Date) => {
    const newParams = new URLSearchParams(searchParams);
    date.setHours(0, 0, 0, 0);
    newParams.set("startDate", date.toISOString());
    setSearchParams(newParams);
  };

  const handleEndDateChange = (date: Date) => {
    const newParams = new URLSearchParams(searchParams);
    date.setHours(23, 59, 59, 999);
    newParams.set("endDate", date.toISOString());
    setSearchParams(newParams);
  };

  const handleClearFilters = () => {
    setSearchParams(new URLSearchParams());
  };

  const studentQuery = useGetStudentById(studentId);

  const headerProps = {
    description: "Veja e gerencie as informações do aluno",
    title: "Detalhes do aluno",
    Icon: GraduationCap,
    backLink: "/students",
  };

  if (studentQuery.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do aluno"
          error={studentQuery.error}
        />
      </PageLayout>
    );
  }

  if (studentQuery.isPending) {
    return (
      <PageLayout {...headerProps}>
        <LoadingCard title="Carregando detalhes do aluno" />
      </PageLayout>
    );
  }

  return (
    <PageLayout {...headerProps}>
      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <SectionCard
          title="Aluno"
          description="Dados do aluno"
          headerActions={
            <div className="flex gap-2 items-center flex-wrap justify-end">
              <Button onClick={() => setIsFormOpen(true)} variant="primary">
                <Edit className="h-4 w-4 mr-2" />
                Editar
              </Button>

              <ArchiveStudentButton
                studentId={studentQuery.data.id}
                isArchived={!!studentQuery.data.archivedAt}
              />
              <DeleteStudentButton studentId={studentQuery.data.id} />
            </div>
          }
        >
          <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
            <SummaryItem label="Nome completo" value={studentQuery.data.name} />
            <SummaryItem label="CPF" value={formatCpf(studentQuery.data.cpf)} />
            <SummaryItem className="col-span-2" label="E-mail" value={studentQuery.data.email} />
            <div className=" flex gap-3 justify-between">
            <SummaryItem className="grow" label="Data de nascimento" value={formatDateShortYear(studentQuery.data.birthdate)} />
            <SummaryItem className="text-center" label="Idade" value={studentQuery.data.age} />
            </div>
            <SummaryItem label="Contato" value={formatPhone(studentQuery.data.contact)}/>
            <SummaryItem label="Data de matrícula" value={formatDateShortYear(studentQuery.data.createdAt)}/>
            <SummaryItem label="Escola" value={studentQuery.data.school} />
            <SummaryItem label="Status" value={studentQuery.data.archivedAt ? "Arquivado" : "Ativo"} />
            <SummaryItem label="Responsável" value={studentQuery.data.responsible?.name} />
            <SummaryItem label="Contato do Responsável" value={formatPhone(studentQuery.data.responsible?.contact)} />
            <SummaryItem label="CPF do Responsável" value={formatCpf(studentQuery.data.responsible?.cpf)} />
          </div>

          <Collapse title={"Endereço"} className="mt-3 shadow-xl hover:bg-base-200/70">
            <AddressDetails address={studentQuery.data.address} />
          </Collapse>
        </SectionCard>

        <div className="flex justify-end gap-2 items-center mb-1">
          <DateRangeInput
            startDate={startDate}
            endDate={endDate}
            onStartDateChange={handleStartDateChange}
            onEndDateChange={handleEndDateChange}
          />
          {(startDate || endDate) && (
            <div className="tooltip" data-tip="Limpar datas">
              <Button size="sm" variant="outline" onClick={handleClearFilters}>
                <BrushCleaning size={16} />
              </Button>
            </div>
          )}
        </div>

        <StudentKPIs studentId={studentId} />

        <StudentEventsTable studentId={studentId} />

        {isFormOpen && (
          <div className="modal modal-open">
            <div className="modal-box max-w-4xl">
              <h3 className="font-bold text-lg mb-4">Editar Aluno</h3>
              <StudentForm
                initialData={studentQuery.data}
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
