export const queryKeys = {
  students: ["students"],
  studentDetail: (id: string) => ["students", id],
  studentsByParent: (id: string) => ["students", "parent", id],
  employees: ["employees"],
  employeeDetail: (id: string) => ["employees", id],
  parents: ["parents"],
  parentDetail: (id: string) => ["parents", id],
  events: ["events"],
  event: (id: string) => ["events", id],
  eventsByStudent: (id: string) => ["events", "student", id],
  eventsByEmployee: (id: string) => ["events", "employee", id],
  dashboard: ["dashboard"],
} as const
