import { ButtonLink } from "@/components/ui/button";
import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { AddressDetails } from "@/features/address/AddressDetails";
import { EventsTable } from "@/features/events/components/EventsTable";
import { useGetEventsByStudent, useGetParentById, useGetStudentById } from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { Edit, GraduationCap } from "lucide-react";
import { useState, type ReactNode } from "react";
import { useParams } from "react-router-dom";
import { ArchiveStudentButton } from "../components/ArchiveStudentButton";
import { DeleteStudentButton } from "../components/DeleteStudentButton";

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const {
    isPending: isStudentPending,
    data: student,
    error: studentError,
  } = useGetStudentById(studentId);


  const studentParent = useGetParentById(student?.parentId || "")

  const {
    isPending: isStudentEventsPending,
    data: studentEvents,
    error: studentEventsError,
  } = useGetEventsByStudent(studentId);

  const isStudentLoading = isStudentPending || !student;
  const hasStudentError = !!studentError;

  const summaryItems: Array<{ label: string; value: ReactNode }> = student ? [
    { label: "Nome completo", value: student.name },
    { label: "CPF", value: formatCpf(student.cpf) },
    { label: "E-mail", value: student.email },
    { label: "Idade", value: student.age },
    { label: "Contato", value: formatPhone(student.contact) },
    {label: "Data de matrícula",value: formatDateShortYear(student.createdAt)},
    { label: "Escola", value: student.school },
    { label: "Status", value: student.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Responsável", value: studentParent.data?.name }
  ] : [];

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
        backLink="/students"
      />

      {hasStudentError ? (
        <ErrorCard
          title="Erro ao carregar detalhes do aluno"
          error={studentError}
        />
      ) : isStudentLoading ? (
        <LoadingCard title="Carregando detalhes do aluno" />
      ) : (
        <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
          <SectionCard
            title="Aluno"
            description="Dados do aluno"
            headerActions={
              <>
                <ButtonLink to={`/students/edit/${student.id}`} variant="primary">
                  <Edit className="h-4 w-4" />
                  Editar
                </ButtonLink>

                <ArchiveStudentButton studentId={student.id} isArchived={!!student.archivedAt} />
                <DeleteStudentButton studentId={student.id} />
              </>
            }
          >
            <div className="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
              {summaryItems.map((item) => (
                <SummaryItem
                  key={item.label}
                  label={item.label}
                  value={item.value}
                />
              ))}
            </div>

            <Collapse title={"Endereço"} className="mt-3 shadow-xl">
              <AddressDetails address={student.address} />
            </Collapse>
          </SectionCard>

          <EventsTable
            eventsPage={studentEvents}
            isPending={isStudentEventsPending}
            error={studentEventsError}
            currentPage={currentPage}
                onPageChange={setCurrentPage}
                description={"Eventos vinculados ao aluno"}
          />
        </div>
      )}
    </>
  );
}
