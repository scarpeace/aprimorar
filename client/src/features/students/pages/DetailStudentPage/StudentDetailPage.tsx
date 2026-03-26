import { useState } from "react";
import { GraduationCap } from "lucide-react";
import { useParams, useNavigate } from "react-router-dom";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import styles from "./student-detail-page.module.css";
import { StudentDetailState } from "./components/StudentDetailState";
import { StudentEventsSection } from "./components/StudentEventsSection";
import { StudentSummarySection } from "./components/StudentSummarySection";
import { AddressSummarySection } from "@/features/address/AddressSumarySection";
import { Collapse } from "@/components/ui/collapse";
import { useEventsByStudent } from "@/features/events/query/eventQueries";
import { useGetStudentById } from "@/kubb";

export function StudentDetailPage() {
  const { id } = useParams<{ id: string }>();
  const studentId = id ?? "";
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(0);

  const {
    data: student,
    isLoading: isStudentLoading,
    error: studentError,
  } = useGetStudentById(studentId);

  const {
    data: studentEvents,
    isLoading: isStudentEventsLoading,
    error: studentEventsError,
  } = useEventsByStudent(studentId);

  return (
    <div className={styles.page}>
      <PageHeader
        description="Veja e gerencie as informações do aluno"
        title="Detalhes do aluno"
        Icon={GraduationCap}
        action={
          <ButtonLink className="sm:ml-auto" to="/students" variant="outline">
            Voltar para alunos
          </ButtonLink>
        }
      />

      <StudentDetailState
        student={student}
        isLoading={isStudentLoading}
        error={studentError}
        onBack={() => navigate("/students")}
      >
        <div className="grid gap-2">
          <StudentSummarySection student={student} studentId={studentId} />
          <Collapse title="Endereço">
            <AddressSummarySection address={student?.address} />
          </Collapse>
        </div>
        <StudentEventsSection
          studentId={studentId}
          events={studentEvents}
          isLoading={isStudentEventsLoading}
          error={studentEventsError}
          currentPage={currentPage}
          onPageChange={setCurrentPage}
        />
      </StudentDetailState>
    </div>
  );
}
