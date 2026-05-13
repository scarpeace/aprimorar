type LinkedStudentSummary = {
  active?: boolean | null;
} & Record<string, unknown>;

type LinkedStudents = LinkedStudentSummary[] | null | undefined;

export function getActiveLinkedStudentsCount(linkedStudents: LinkedStudents) {
  return (linkedStudents ?? []).filter(
    (student: LinkedStudentSummary) => student.active !== false,
  ).length;
}
