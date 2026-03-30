import { useState } from "react";
import { GraduationCap } from "lucide-react";
import { useParams } from "react-router-dom";
import { PageHeader } from "@/components/ui/page-header";
import { AddressSummarySection } from "@/features/address/AddressSumarySection";
import { useEventsByStudent } from "@/features/events/query/eventQueries";
import { StudentEventsSection } from "../components/StudentEventsSection";
import { StudentSummarySection } from "../components/StudentSummarySection";
import { useStudentByIdQuery } from "../hooks/use-students-query";
import { ErrorCard } from "@/components/ui/error-card";
import { LoadingCard } from "@/components/ui/loading-card";
import { ArchiveStudentButton } from "../components/ArchiveStudentButton";
import { DeleteStudentButton } from "../components/DeleteStudentButton";
import { EditStudentButton } from "../components/EditStudentButton";

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const [currentPage, setCurrentPage] = useState(0);

  const studentQuery = useStudentByIdQuery({ studentId });
  const studentEventsQuery = useEventsByStudent(studentId);

  if (studentQuery.isError) {
    return (
      <ErrorCard title="Erro ao carregar aluno" error={studentQuery.error} />
    );
  }

  if (studentQuery.isPending) {
    return <LoadingCard title="Carregando listagem de alunos" />;
  }

  return (
    <div className=" flex flex-col gap-5">
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
      >
      <div className="flex flex-row ml-auto mt-auto gap-3  min-w-0 animate-[fade-up_300ms_ease-out_both]">
          <EditStudentButton studentId={studentId} />
          <ArchiveStudentButton
            studentId={studentId}
            isArchived={!!studentQuery.data.archivedAt}
          />
          <DeleteStudentButton studentId={studentId} />
        </div>
      </PageHeader>

      <div className="grid gap-2 animate-[fade-up_300ms_ease-out_both]">
        <StudentSummarySection student={studentQuery.data} />
        <AddressSummarySection address={studentQuery.data.address} />
      </div>

      <StudentEventsSection
        events={studentEventsQuery.data?.content}
        isLoading={studentEventsQuery.isPending}
        error={studentEventsQuery.error}
        currentPage={currentPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}
