import { ButtonLink } from "@/components/ui/button";
import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageHeader } from "@/components/ui/page-header";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { EventsTable } from "@/features/events/components/EventsTable";
import {
  useGetEventsByStudent,
  useGetParentById,
  useGetStudentById,
} from "@/kubb";
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
import { AddressDetails } from "@/features/address/components/AddressDetails";

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const studentQuery = useGetStudentById(studentId);
  const studentParent = useGetParentById(studentQuery.data?.parentId || "");
  const studentEvents = useGetEventsByStudent(studentId);

  const summaryItems: Array<{ label: string; value: ReactNode }> = studentQuery.data
    ? [
        { label: "Nome completo", value: studentQuery.data.name },
        { label: "CPF", value: formatCpf(studentQuery.data.cpf) },
        { label: "E-mail", value: studentQuery.data.email },
        { label: "Idade", value: studentQuery.data.age },
        { label: "Contato", value: formatPhone(studentQuery.data.contact) },
        {
          label: "Data de matrícula",
          value: formatDateShortYear(studentQuery.data.createdAt),
        },
        { label: "Escola", value: studentQuery.data.school },
        { label: "Status", value: studentQuery.data.archivedAt ? "Arquivado" : "Ativo" },
        { label: "Responsável", value: studentParent.data?.name },
        { label: "Contato do Responsável", value: studentParent.data?.contact },
        { label: "CPF do Responsável", value: formatCpf(studentParent.data?.cpf || "") },
      ]
    : [];

  return (
    <>
      <PageHeader
      description="Veja e gerencie as informações do aluno"
      title="Detalhes do aluno"
      Icon={GraduationCap}
      backLink="/students"
      />

      {studentQuery.isError ? (
        <ErrorCard
          title="Erro ao carregar detalhes do aluno"
          error={studentQuery.error}
        />
      ) : studentQuery.isPending || !studentQuery.data ? (
        <LoadingCard title="Carregando detalhes do aluno" />
      ) : (
        <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
          <SectionCard
            title="Aluno"
            description="Dados do aluno"
            headerActions={
              <>
                <ButtonLink
                  to={`/students/edit/${studentQuery.data.id}`}
                  variant="primary"
                >
                  <Edit className="h-4 w-4" />
                  Editar
                </ButtonLink>

                <ArchiveStudentButton
                  studentId={studentQuery.data.id}
                  isArchived={!!studentQuery.data.archivedAt}
                />
                <DeleteStudentButton studentId={studentQuery.data.id} />
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
              <AddressDetails address={studentQuery.data.address} />
            </Collapse>
          </SectionCard>

          <SectionCard title={"Atendimentos"} description={"Atendimentos vinculados ao aluno"}>
            <EventsTable
              eventsPage={studentEvents.data}
              isPending={studentEvents.isPending}
              error={studentEvents.error}
              currentPage={currentPage}
              onPageChange={setCurrentPage}
            />
          </SectionCard>
        </div>
      )}
    </>
  );
}
