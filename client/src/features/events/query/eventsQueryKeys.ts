export const eventsQueryKeys = {
  all: ["events"] as const,
  lists: () => ["events", "list"] as const,
  list: (params: Record<string, unknown>) => ["events", "list", params] as const,
  details: () => ["events", "detail"] as const,
  detail: (id: string) => ["events", "detail", id] as const,
  byStudentPrefix: () => ["events", "by-student"] as const,
  byStudent: (studentId: string) => ["events", "by-student", studentId] as const,
  byEmployeePrefix: () => ["events", "by-employee"] as const,
  byEmployee: (employeeId: string) => ["events", "by-employee", employeeId] as const,
} as const
