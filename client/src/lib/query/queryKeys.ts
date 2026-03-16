export const queryKeys = {
  students: {
    all: ["students"] as const,
    lists: () => ["students", "list"] as const,
    list: (params: Record<string, unknown>) => ["students", "list", params] as const,
    details: () => ["students", "detail"] as const,
    detail: (id: string) => ["students", "detail", id] as const,
    byParent: (parentId: string) => ["students", "by-parent", parentId] as const,
    options: ["students", "options"] as const,
  },
  employees: {
    all: ["employees"] as const,
    lists: () => ["employees", "list"] as const,
    list: (params: Record<string, unknown>) => ["employees", "list", params] as const,
    details: () => ["employees", "detail"] as const,
    detail: (id: string) => ["employees", "detail", id] as const,
    options: ["employees", "options"] as const,
  },
  parents: {
    all: ["parents"] as const,
    lists: () => ["parents", "list"] as const,
    list: (params: Record<string, unknown>) => ["parents", "list", params] as const,
    details: () => ["parents", "detail"] as const,
    detail: (id: string) => ["parents", "detail", id] as const,
    options: ["parents", "options"] as const,
  },
  events: {
    all: ["events"] as const,
    lists: () => ["events", "list"] as const,
    list: (params: Record<string, unknown>) => ["events", "list", params] as const,
    details: () => ["events", "detail"] as const,
    detail: (id: string) => ["events", "detail", id] as const,
    byStudent: (studentId: string) => ["events", "by-student", studentId] as const,
    byEmployee: (employeeId: string) => ["events", "by-employee", employeeId] as const,
  },
  dashboard: {
    all: ["dashboard"] as const,
  },
} as const
