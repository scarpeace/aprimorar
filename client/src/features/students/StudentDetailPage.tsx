import { useState, type ReactNode } from "react";
import { GraduationCap } from "lucide-react";
import { useParams, useNavigate } from "react-router-dom";
import { ButtonLink } from "@/components/ui/button";
import { ErrorCard } from "@/components/ui/error-card";
import { PageHeader } from "@/components/ui/page-header";
import { PageLoading } from "@/components/ui/page-loading";
import { SectionCard } from "@/components/ui/section-card";
import { SummaryItem } from "@/components/ui/summary-item";
import styles from "@/features/students/StudentDetailPage.module.css";
import { formatDateShortYear } from "@/lib/utils/formatter";
import { getFriendlyErrorMessage } from "@/lib/shared/api";
import { EditStudentButton } from "./components/EditStudentButton";
import { ArchiveStudentButton } from "./components/ArchiveStudentButton";
import { DeleteStudentButton } from "./components/DeleteStudentButton";
import { useGetEventsByStudent } from "@/kubb";
import { EventTable } from "@/features/events/components/EventTable";
import { useStudentById } from "./query/studentQueries";

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(0);
  const pageSize = 10;

  const {
    data: student,
    isLoading: isStudentLoading,
    error: studentError,
    isFetched: isStudentFetched,
  } = useStudentById(studentId);

  const {
    data: studentEvents,
    isLoading: isStudentEventsLoading,
    error: studentEventsError,
  } = useGetEventsByStudent(studentId, {
    page: currentPage,
    size: pageSize,
    sort: ["startDate,desc"],
  });

  const summaryItems: Array<{ label: string; value: ReactNode }> = [
    { label: "Nome completo", value: student?.name },
    { label: "CPF", value: student?.cpf },
    { label: "E-mail", value: student?.email },
    { label: "Idade", value: student?.age },
    { label: "Contato", value: student?.contact },
    { label: "Data de nascimento", value: student?.birthdate },
    {
      label: "Data de matrícula",
      value: formatDateShortYear(student?.createdAt ?? ""),
    },
    { label: "Escola", value: student?.school },
    { label: "Status", value: student?.archivedAt ? "Arquivado" : "Ativo" },
    { label: "Responsável", value: student?.parent.name },
    { label: "E-mail do responsável", value: student?.parent.email },
    { label: "Contato do responsável", value: student?.parent.contact },
    { label: "CPF do responsável", value: student?.parent.cpf },
    { label: "Endereço", value: student?.address.street },
    {
      label: "Complemento",
      value: student?.address.complement ?? "Sem complemento",
    },
    { label: "CEP", value: student?.address.zip },
  ];

  return (
    <div className={styles.page}>
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
        action={
          <ButtonLink
            className="sm:ml-auto"
            to="/students/new"
            variant="success"
          >
            Novo aluno
          </ButtonLink>
        }
      />

      <SectionCard
        title="Resumo do aluno"
        description="Dados de aluno, responsável e endereço em um único resumo."
        headerAction={
          <>
            <EditStudentButton studentId={studentId} />
            <ArchiveStudentButton studentId={studentId} isArchived={!!student?.archivedAt} />
            <DeleteStudentButton studentId={studentId}  />
          </>
        }
      >
        {isStudentLoading && <PageLoading message="Carregando aluno..." />}

        {studentError && (
          <div className={styles.page}>
            <ErrorCard
              description={getFriendlyErrorMessage(studentError)}
              actionLabel="Voltar para listagem de alunos"
              onAction={() => navigate("/students")}
            />
          </div>
        )}

        {isStudentFetched && student && (
          <div className={styles.summaryGrid}>
            {summaryItems.map((item) => (
              <SummaryItem
                key={item.label}
                label={item.label}
                value={item.value}
              />
            ))}
          </div>
        )}
      </SectionCard>

      {/* EVENTOS DO ALUNO */}
      <SectionCard
        title="Eventos vinculados"
        description="Todos os eventos vinculados a este aluno."
      >
        {/*<EventsTable variant="embeddedStudent" ownerId={studentId} />*/}
        <EventTable
          variant="page"
          context="student"
          data={studentEvents}
          isLoading={isStudentEventsLoading}
          error={studentEventsError ?? null}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
          itemName="eventos"
          renderActions={(event) => (
            <ButtonLink to={`/events/${event.eventId}`} size="sm" variant="outline">
              Detalhes
            </ButtonLink>
          )}
        />
      </SectionCard>
    </div>
  );
}
