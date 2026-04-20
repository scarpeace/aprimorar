import type { PageDTOStudentResponseDTO, StudentResponseDTO } from "@/kubb";

type LinkedStudentsPage = Pick<PageDTOStudentResponseDTO, "content"> | null | undefined;
type LinkedStudentSummary = Pick<StudentResponseDTO, "archivedAt">;

export function getActiveLinkedStudentsCount(linkedStudentsPage: LinkedStudentsPage) {
  return (linkedStudentsPage?.content ?? []).filter(
    (student: LinkedStudentSummary) => student.archivedAt == null,
  ).length;
}
