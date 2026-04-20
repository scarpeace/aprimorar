type LinkedStudentSummary = {
  archivedAt?: string | null;
} & Record<string, unknown>;

type LinkedStudentsPage = {
  content: LinkedStudentSummary[];
} | null | undefined;

export function getActiveLinkedStudentsCount(linkedStudentsPage: LinkedStudentsPage) {
  return (linkedStudentsPage?.content ?? []).filter(
    (student: LinkedStudentSummary) => student.archivedAt == null,
  ).length;
}
