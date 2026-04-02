import { Collapse } from "@/components/ui/collapse";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { PageHeader } from "@/components/ui/page-header";
import { AddressSummarySection } from "@/features/address/AddressSumarySection";
import { EventsTable } from "@/features/events/components/EventsTable";
import { GraduationCap } from "lucide-react";
import { useState } from "react";
import { useParams } from "react-router-dom";
import { ArchiveStudentButton } from "../components/ArchiveStudentButton";
import { DeleteStudentButton } from "../components/DeleteStudentButton";
import { EditStudentButton } from "../components/EditStudentButton";
import { StudentDetails } from "../components/StudentDetails";
import { useStudentById } from "../hooks/use-students-query";
import { useEventsByStudent } from "@/features/events/hooks/use-event-queries";

export function StudentDetailsPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: student,
    isError: isStudentError,
    isPending: isStudentPending,
    error: studentError,
  } = useStudentById({ studentId });
  const {
    data: studentEvents,
    isPending: isStudentEventsPending,
    error: studentEventsError,
  } = useEventsByStudent(studentId);


  if (isStudentError) {
    return <ErrorCard title="Erro ao carregar aluno" error={studentError} />;
  }

  //TODO: colocar isLoading aqui também em todas as páginas que tem esse tratamento de estado
// as vezes é até bom criar um componente reutilizavel
  if (isStudentPending) {
    return <LoadingCard title="Carregando dados do aluno" />;
  }

  return (
    <>
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
      >
        <div className="flex flex-row ml-auto mt-auto gap-3">
          <EditStudentButton studentId={studentId} />
          <ArchiveStudentButton
            studentId={studentId}
            isArchived={!!student.archivedAt}
          />
          <DeleteStudentButton studentId={studentId} />
        </div>
      </PageHeader>

      <div className="grid gap-3 animate-[fade-up_300ms_ease-out_both]">
        <StudentDetails student={student} />
        <Collapse title={"Endereço"}>
          <AddressSummarySection address={student.address} />
        </Collapse>

        <EventsTable
          eventsPage={studentEvents}
          isPending={isStudentEventsPending}
          error={studentEventsError}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </div>
    </>
  );
}
