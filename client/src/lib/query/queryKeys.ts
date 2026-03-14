type ListParams = {
  page: number
  size: number
  sortBy: string
}

export const queryKeys = {
  students: {
    all: ["students"] as const,
    lists: () => [...queryKeys.students.all, "list"] as const,
    list: (params: ListParams) => [...queryKeys.students.lists(), params] as const,
    details: () => [...queryKeys.students.all, "detail"] as const,
    detail: (id: string) => [...queryKeys.students.details(), id] as const,
    editDetails: () => [...queryKeys.students.all, "editDetail"] as const,
    editDetail: (id: string) => [...queryKeys.students.editDetails(), id] as const,
  },
  employees: {
    all: ["employees"] as const,
    lists: () => [...queryKeys.employees.all, "list"] as const,
    list: (params: ListParams) => [...queryKeys.employees.lists(), params] as const,
    details: () => [...queryKeys.employees.all, "detail"] as const,
    detail: (id: string) => [...queryKeys.employees.details(), id] as const,
    editDetails: () => [...queryKeys.employees.all, "editDetail"] as const,
    editDetail: (id: string) => [...queryKeys.employees.editDetails(), id] as const,
  },
  parents: {
    all: ["parents"] as const,
    lists: () => [...queryKeys.parents.all, "list"] as const,
    list: (params: ListParams) => [...queryKeys.parents.lists(), params] as const,
    details: () => [...queryKeys.parents.all, "detail"] as const,
    detail: (id: string) => [...queryKeys.parents.details(), id] as const,
  },
  events: {
    all: ["events"] as const,
    lists: () => [...queryKeys.events.all, "list"] as const,
    list: (params: ListParams) => [...queryKeys.events.lists(), params] as const,
    details: () => [...queryKeys.events.all, "detail"] as const,
    detail: (id: string) => [...queryKeys.events.details(), id] as const,
    editDetails: () => [...queryKeys.events.all, "editDetail"] as const,
    editDetail: (id: string) => [...queryKeys.events.editDetails(), id] as const,
    byStudentRoot: (studentId: string) => [...queryKeys.events.all, "byStudent", studentId] as const,
    byStudent: (studentId: string, params: ListParams) => [...queryKeys.events.byStudentRoot(studentId), params] as const,
    byEmployeeRoot: (employeeId: string) => [...queryKeys.events.all, "byEmployee", employeeId] as const,
    byEmployee: (employeeId: string, params: ListParams) => [...queryKeys.events.byEmployeeRoot(employeeId), params] as const,
    createOptions: () => [...queryKeys.events.all, "createOptions"] as const,
  },
  dashboard: {
    summary: () => ["dashboard", "summary"] as const,
  },
}
