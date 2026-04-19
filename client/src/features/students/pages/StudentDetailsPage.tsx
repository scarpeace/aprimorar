import { ButtonLink } from "@/components/ui/button";
import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import { PageLayout } from "@/components/layout/PageLayout";
import { AddressDetails } from "@/features/address/components/AddressDetails";
import { EventsTable } from "@/features/events/components/EventsTable";
import {
  useGetEventsByStudent,
  useGetStudentById,
} from "@/kubb";
import {
  formatCpf,
  formatDateShortYear,
  formatPhone,
} from "@/lib/utils/formatter";
import { Edit, GraduationCap } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { ArchiveStudentButton } from "../components/ArchiveStudentButton";
import { DeleteStudentButton } from "../components/DeleteStudentButton";

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const studentQuery = useGetStudentById(studentId);
  const studentEvents = useGetEventsByStudent(studentId);

  const headerProps = {
    description: "Veja e gerencie as informações do aluno",
    title: "Detalhes do aluno",
    Icon: GraduationCap,
    backLink: "/students",
  };

  if (studentQuery.isError || studentEvents.isError) {
    return (
      <PageLayout {...headerProps}>
        <ErrorCard
          title="Erro ao carregar detalhes do aluno"
          error={
            studentQuery.error || studentEvents.error
          }
        />
      </PageLayout>
    );
  }

  if (
    studentQuery.isPending ||
    studentEvents.isPending
  ) {
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

          <Collapse title={"Endereço"} className="mt-3 shadow-xl">
            <AddressDetails address={studentQuery.data.address} />
          </Collapse>
        </SectionCard>

        <SectionCard
          title={"Atendimentos"}
          description={"Atendimentos vinculados ao aluno"}
        >
          <EventsTable
            eventsPage={studentEvents.data}
            isPending={studentEvents.isPending}
            error={studentEvents.error}
            currentPage={currentPage}
            onPageChange={setCurrentPage}
          />
        </SectionCard>
      </div>
    </PageLayout>
  );
}
