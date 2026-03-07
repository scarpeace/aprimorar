export const STUDENT_PAGE_SIZE_OPTIONS = [10, 20, 50] as const

export function buildStudentFiltersLabel(nameFilter: string, hideArchived: boolean) {
  const filters: string[] = []

  if (nameFilter) {
    filters.push(`nome: "${nameFilter}"`)
  }

  filters.push(hideArchived ? "somente ativos" : "inclui arquivados")

  return filters.join(" • ")
}
