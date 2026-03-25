import { useState } from "react";
import { GraduationCap } from "lucide-react";
import { useParams, useNavigate } from "react-router-dom";
import { ButtonLink } from "@/components/ui/button";
import { PageHeader } from "@/components/ui/page-header";
import styles from "@/features/students/StudentDetailPage.module.css";
import { useGetEventsByStudent } from "@/kubb";
import { useStudentById } from "./query/studentQueries";
import { StudentDetailState } from "./components/StudentDetailState";
import { StudentEventsSection } from "./components/StudentEventsSection";
import { StudentSummarySection } from "./components/StudentSummarySection";

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
        <StudentSummarySection student={student} studentId={studentId} />

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
